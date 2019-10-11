package cs3500.animator.model;

import java.awt.Point;
import java.awt.Color;

/**
 * Represents an ellipse as a Shape in the animation.
 */
public class Ellipse extends AShape {

  /**
   * Constructs an ellipse with the given parameters.
   *
   * @param height height in pixels
   * @param width  width in pixels
   * @param x      x position
   * @param y      y position
   * @param r      color red value
   * @param g      color blue value
   * @param b      color green value
   */
  public Ellipse(int height, int width, int x, int y, int r, int g, int b, String name) {
    super(height, width, new Point.Double(x, y), new Color(r, g, b), name);
  }

  @Override
  public String getType() {
    return "ellipse";
  }

  @Override
  public String toString() {
    return "Ellipse: " + getName() + ". " + getHeight() + "x" + getWidth() + " at (" +
            getPosition() + ". Color: " + getColor() + ". ";
  }
}
