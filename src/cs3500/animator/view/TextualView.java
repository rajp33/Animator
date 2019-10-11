package cs3500.animator.view;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import cs3500.animator.model.Animation;

/**
 * A textual representation of an Animation.
 */
public class TextualView extends AView {

  /**
   * Constructs a TextualView given the animation and name of the file that this view will render
   * to.
   *
   * @param animation  animation to read from
   * @param outputName name of file to write to
   */
  TextualView(Animation animation, String outputName) {
    super(animation, outputName);
    if (outputName != null) {
      //add extension if not already present
      outputName = outputName.endsWith(".txt") ? outputName : outputName + ".txt";
      //set outputstream
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
    System.out.println("canvas " + this.x + " " + this.y + " " + this.width + " " + this.height);
    System.out.println(animation.status());
  }

  @Override
  public void refresh(int time) {
    throw new UnsupportedOperationException("A TextualView should not be refreshed.");
  }
}
