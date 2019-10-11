package cs3500.animator.model;


import java.awt.Color;

/**
 * Represents the basic functionality a keyframe should have.
 */
public interface Keyframe extends Comparable<Keyframe>, ReadOnlyKeyframe {

  void setTime(int time);

  void setDimensions(double height, double width);

  void setColor(Color color);

  void setPosition(double x, double y);
}
