package cs3500.animator.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cs3500.animator.model.ShapeFactory;
import cs3500.animator.util.AnimationBuilder;

/**
 * Represents a builder for an SVG type file given an animation.
 */
public class SVGAnimationBuilder implements AnimationBuilder<String> {
  private final StringBuilder output = new StringBuilder();
  private final Map<String, List<String>> shapeDecl;
  private final int speed; //ticks/sec

  /**
   * Constructs an SVGAnimationBuilder given the speed of the animation.
   *
   * @param speed speed of the animation.
   */
  public SVGAnimationBuilder(int speed) {
    this.speed = speed;
    shapeDecl = new LinkedHashMap<>();
  }

  @Override
  public String build() {
    for (String shapeName : shapeDecl.keySet()) {
      List<String> shape = shapeDecl.get(shapeName);
      String type = shape.remove(0);
      for (int i = 0; i < shape.size(); i++) {
        String line = shape.get(i);
        if (i == 0) {
          output.append(line).append(">\n");
        } else {
          output.append(line).append(" fill=\"freeze\"/>\n");
        }
      }
      output.append("</").append(type).append(">\n");
    }
    output.append("</svg>");
    return output.toString();
  }

  @Override
  public AnimationBuilder<String> setBounds(int x, int y, int width, int height) {
    output.append("<svg width=\"").append(width).append("\" height=\"")
            .append(height).append("\" version=\"1.1\"")
            .append(" xmlns=\"http://www.w3.org/2000/svg\">\n");
    return this;
  }

  @Override
  public AnimationBuilder<String> declareShape(String name, String type) {
    List<String> lines = new ArrayList<>();
    String svgName = ShapeFactory.getSvgName(type);
    lines.add(svgName);
    lines.add("<" + ShapeFactory.getSvgName(type) + " id=\"" + name + "\" " +
            ShapeFactory.getSVGAttributeName(svgName, "x") + "=\"0\" " +
            ShapeFactory.getSVGAttributeName(svgName, "y") + "=\"0\" " +
            ShapeFactory.getSVGAttributeName(svgName, "w") + "=\"0\" " +
            ShapeFactory.getSVGAttributeName(svgName, "h") + "=\"0\" " +
            "fill=\"rgb(0,0,0)\" visibility=\"hidden\"");
    shapeDecl.put(name, lines);
    return this;
  }

  @Override
  public AnimationBuilder<String> addMotion(String name, int t1, int x1, int y1, int w1, int h1,
                                            int r1, int g1, int b1, int t2, int x2, int y2, int w2,
                                            int h2, int r2, int g2, int b2) {
    String animateBase =
            "<animate attributeType=\"xml\" begin=\"" + this.convertToMs(t1)
                    + "ms\" dur=\"" + (this.convertToMs(t2) - this.convertToMs(t1)) + "ms\" ";
    if (shapeDecl.containsKey(name)) {
      List<String> lines = shapeDecl.get(name);
      String type = lines.get(0);
      if (lines.size() == 2) {
        lines.add("<set attributeName=\"visibility\" to=\"visible\" begin=\""
                + this.convertToMs(t1) + "ms\" dur=\"0.0ms\"");
        lines.add("<set attributeName=\"fill\" to=\"rgb(" + r1 + "," + g1 + "," + b1 + ")\" "
                + "begin=\"" + this.convertToMs(t1) + "ms\" dur=\"0.0ms\"");
        lines.add(generateAttributeSet(x1, "x", type, this.convertToMs(t1)));
        lines.add(generateAttributeSet(y1, "y", type, this.convertToMs(t1)));
        lines.add(generateAttributeSet(h1, "h", type, this.convertToMs(t1)));
        lines.add(generateAttributeSet(w1, "w", type, this.convertToMs(t1)));
      }
      if (x1 != x2) {
        lines.add(animateBase
                + this.generateAttributeChange(x1, x2, "x", type));
      }
      if (y1 != y2) {
        lines.add(animateBase
                + this.generateAttributeChange(y1, y2, "y", type));
      }
      if (h1 != h2) {
        lines.add(animateBase
                + this.generateAttributeChange(h1, h2, "h", type));
      }
      if (w1 != w2) {
        lines.add(animateBase
                + this.generateAttributeChange(w1, w2, "w", type));
      }
      if (r1 != r2 || g1 != g2 || b1 != b2) {
        lines.add(animateBase
                + "attributeName=\"fill\" from=\"rgb(" + r1 + "," + g1 + "," + b1 + ")\""
                + " to=\"rgb(" + r2 + "," + g2 + "," + b2 + ")\"");
      }
    }
    return this;
  }

  @Override
  public AnimationBuilder<String> addKeyframe(String name, int t, int x, int y,
                                              int w, int h, int r, int g, int b) {
    throw new UnsupportedOperationException("SVG does not support keyframes.");
  }

  /**
   * Convert tick to milliseconds based on speed given.
   *
   * @param tick tick number
   * @return tick time in milliseconds
   */
  private int convertToMs(int tick) {
    return tick * (1000 / speed);
  }

  /**
   * Generate attribute change stubs for animate elements in svg.
   *
   * @param from          from value
   * @param to            to value
   * @param attributeName name of attribute being changed
   * @param shapeType     shape type as described by svg
   * @return stub to be used in an animate element in svg
   */
  private String generateAttributeChange(int from, int to, String attributeName, String shapeType) {
    String attr = ShapeFactory.getSVGAttributeName(shapeType, attributeName);
    return "attributeName=\"" + attr + "\"" + " from=\"" + from + "\"" + " to=\"" + to + "\"";
  }

  /**
   * Generates a set element for changing attributes at a certain time.
   *
   * @param to            value to change to
   * @param attributeName attribute name
   * @param shapeType     shapeType in svg
   * @param time          time
   * @return a line of xml that describes setting an attribute, without the ending seperator.
   */
  private String generateAttributeSet(int to, String attributeName, String shapeType, int time) {
    String attr = ShapeFactory.getSVGAttributeName(shapeType, attributeName);
    return "<set attributeName=\"" + attr + "\"" + " to=\"" + to + "\" "
            + "begin=\"" + time + "ms\" dur=\"0.0ms\"";
  }
}