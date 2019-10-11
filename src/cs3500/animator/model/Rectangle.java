package cs3500.animator.model;

import java.awt.Color;
import java.awt.Point;

/**
 * Represents a rectangle in the animation.
 */
public class Rectangle extends AShape {

  /**
   * Constructs a rectangle with the given parameters.
   *
   * @param height height in pixels
   * @param width  width in pixels
   * @param x      x position
   * @param y      y position
   * @param r      Color red value
   * @param g      Color blue value
   * @param b      Color green value
   * @param name   name of the shape
   */
  public Rectangle(int height, int width, int x, int y, int r, int g, int b, String name) {
    super(height, width, new Point.Double(x, y), new Color(r, g, b), name);
  }

  @Override
  public String getType() {
    return "rectangle";
  }

  @Override
  public String toString() {
    return "Rectangle: " + getName() + " . " + getHeight() + "x" + getWidth() + ",  at "
            + getPosition() + ". Color: " + getColor();
  }
}
