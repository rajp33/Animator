package cs3500.animator.model;

import java.awt.Color;
import java.awt.Point;


/**
 * Represents a keyframe with read-only access (only getters).
 */
public interface ReadOnlyKeyframe {

  /**
   * Returns the time the keyframe is at.
   *
   * @return time in ticks
   */
  int getTime();

  /**
   * Returns the color the keyframe specifies.
   *
   * @return color object
   */
  Color getColor();

  /**
   * Returns the position the keyframe specifies.
   *
   * @return point object.
   */
  Point.Double getPosition();

  /**
   * Returns the height that the keyframe specifies.
   *
   * @return height
   */
  double getHeight();

  /**
   * Returns the width the keyframe specifies.
   *
   * @return width
   */
  double getWidth();

}
