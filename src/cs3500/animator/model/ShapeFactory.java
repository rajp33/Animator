package cs3500.animator.model;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a factory for creating shapes based on a string representation of their names. This
 * enum should be updated every time a shape implementation is added.
 */
public enum ShapeFactory {
  Ellipse("ellipse", (String name)
      -> new Ellipse(0, 0, 0, 0, 0, 0, 0, name),
      (Graphics g, ReadOnlyShape shape)
          -> g.fillOval((int) shape.getPosition().x, (int) shape.getPosition().y,
          (int) shape.getWidth(), (int) shape.getHeight()),
      "ellipse",
      new HashMap<String, String>() {
        {
          put("x", "cx");
          put("y", "cy");
          put("h", "ry");
          put("w", "rx");
        }
      }
  ),
  Rectangle("rectangle", (String name)
      -> new Rectangle(0, 0, 0, 0, 0, 0, 0, name),
      (Graphics g, ReadOnlyShape shape)
          -> g.fillRect((int) shape.getPosition().x, (int) shape.getPosition().y,
          (int) shape.getWidth(), (int) shape.getHeight()),
      "rect",
      new HashMap<String, String>() {
        {
          put("x", "x");
          put("y", "y");
          put("h", "height");
          put("w", "width");
        }
      });

  private final ShapeFactoryFunctionalInterface sfactory;
  private final String type;
  private final ShapeDrawFunctionalInterface shapeDraw;
  private final String svgName;
  private final Map<String, String> attrMap;

  /**
   * Constructor for ShapeFactory.
   *
   * @param type      string representation of shape
   * @param sfactory  function object to create shape
   * @param shapeDraw function object to draw shape
   * @param svgName   string representation for svg
   */
  ShapeFactory(String type, ShapeFactoryFunctionalInterface sfactory,
               ShapeDrawFunctionalInterface shapeDraw, String svgName, Map<String, String> map) {
    this.type = type;
    this.sfactory = sfactory;
    this.shapeDraw = shapeDraw;
    this.svgName = svgName;
    this.attrMap = map;
  }

  /**
   * Creates a shape based on the type of Shape given. Should create a shape with parameters of
   * zero.
   *
   * @param name name to make the shape
   * @param type type of the shape
   * @return Shape f given type
   * @throws IllegalArgumentException when the shape type is not found.
   */
  public static Shape createShape(String name, String type) {
    for (ShapeFactory types : ShapeFactory.values()) {
      if (types.type.equalsIgnoreCase(type)) {
        return types.sfactory.create(name);
      }
    }
    throw new IllegalArgumentException("Shape type: " + type + " does not exist");
  }

  /**
   * Draws the given shape onto the given graphics object.
   *
   * @param shape shape to draw
   * @param g     graphics object
   */
  public static void draw(ReadOnlyShape shape, Graphics g) {
    boolean found = false;
    for (ShapeFactory types : ShapeFactory.values()) {
      if (types.type.equalsIgnoreCase(shape.getType())) {
        found = true;
        g.setColor(shape.getColor());
        types.shapeDraw.draw(g, shape);
      }
    }
    if (!found) {
      throw new IllegalArgumentException("Shape type: " + shape.getType() + " does not exist");
    }
  }

  /**
   * Gets the svg attribute name depending on the type of shape.
   *
   * @param svgName   name of shape according to svg format
   * @param paramName parameter name to fetch
   * @return svg attribute that corresponds to the parameter and shape specified
   */
  public static String getSVGAttributeName(String svgName, String paramName) {
    for (ShapeFactory types : ShapeFactory.values()) {
      if (types.svgName.equalsIgnoreCase(svgName)) {
        return types.attrMap.get(paramName);
      }
    }
    throw new IllegalArgumentException("Shape with SVG Name: " + svgName + " does not exist");
  }

  /**
   * Returns the SVG identifier for this type of shape.
   *
   * @return the svg identifier.
   */
  public static String getSvgName(String type) {
    for (ShapeFactory types : ShapeFactory.values()) {
      if (types.type.equalsIgnoreCase(type)) {
        return types.svgName;
      }
    }
    throw new IllegalArgumentException("Shape type: " + type + " does not exist");
  }

  /**
   * Represents a functional interface that creates a specific implementation of a shape given the
   * implementation's type.
   */
  @FunctionalInterface
  private interface ShapeFactoryFunctionalInterface {
    /**
     * Creates the Shape with the given name.
     *
     * @param name shape name
     * @return a Shape with the type that this enum represents and the given name.
     */
    Shape create(String name);
  }

  /**
   * Represents a functional interface that draws the specific implementation of 2DGraphics that the
   * shape represents.
   */
  @FunctionalInterface
  private interface ShapeDrawFunctionalInterface {
    /**
     * Draws the shape onto the given Graphics.
     *
     * @param g     Graphics object to draw on
     * @param shape Shape object to draw
     */
    void draw(Graphics g, ReadOnlyShape shape);
  }
}
