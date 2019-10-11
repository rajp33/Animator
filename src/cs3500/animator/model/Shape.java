package cs3500.animator.model;

import java.awt.Color;
import java.awt.Point;

/**
 * Represents a Shape to be used with an {@link Animation}. Each Shape should have a unique name.
 */
public interface Shape extends MotionListener, ReadOnlyShape {

  /**
   * Set the height of this Shape.
   *
   * @param h height
   */
  void setHeight(double h);

  /**
   * Set the width of this Shape.
   *
   * @param w width
   */
  void setWidth(double w);

  /**
   * Set the Color of this Shape.
   *
   * @param color color
   */
  void setColor(Color color);

  /**
   * Set the position of this Shape.
   *
   * @param newPosition position
   */
  void setPosition(Point.Double newPosition);
}
