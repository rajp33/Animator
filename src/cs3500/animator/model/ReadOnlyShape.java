package cs3500.animator.model;

import java.awt.Color;
import java.awt.Point;

/**
 * Represents a read-only shape. The shapes paramaters are allowed to be read but are not allowed to
 * be set.
 */
public interface ReadOnlyShape {

  /**
   * Returns the height of this Shape.
   *
   * @return height
   */
  double getHeight();

  /**
   * Returns the width of this Shape.
   *
   * @return width
   */
  double getWidth();

  /**
   * Returns the color of this Shape.
   *
   * @return color
   */
  Color getColor();

  /**
   * Returns the Position of this Shape.
   *
   * @return position
   */
  Point.Double getPosition();

  /**
   * Get the name of this Shape.
   *
   * @return name
   */
  String getName();

  /**
   * Returns the type of shape. For example: ellipse, rectangle, triangle, etc.
   *
   * @return the type of shape
   */
  String getType();
}
