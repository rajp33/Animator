package cs3500.animator.view;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import cs3500.animator.model.Animation;
import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.util.AnimationReader;

/**
 * Represents a ViewModel that renders an {@link Animation} to an SVG file.
 */
public class SVGAnimationView extends AView {

  /**
   * Constructs an SVGAnimationView by reading the animation.
   *
   * @param animation  animation to read from.
   * @param outputName name of file to output to.
   */
  SVGAnimationView(Animation animation, String outputName) {
    super(animation, outputName);
    if (outputName != null) {
      outputName = outputName.endsWith(".svg") ? outputName : outputName + ".svg";
      try {
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(
                outputName)), true));
      } catch (FileNotFoundException e) {
        throw new IllegalStateException("Error creating file: " + e.getMessage());
      }
    }
  }

  @Override
  public void render() {
    AnimationBuilder<String> svgBuilder = new SVGAnimationBuilder(speed);
    String animationStatus = "canvas " + x + " " + y + " "
            + width + " " + height + "\n" + animation.status();
    Readable status = new StringReader(animationStatus);
    System.out.println(AnimationReader.parseFile(status, svgBuilder));
  }

  @Override
  public void refresh(int time) {
    throw new UnsupportedOperationException("A SVG View should not be refreshed.");
  }
}
