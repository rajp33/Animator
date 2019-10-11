package cs3500.animator.model;

import java.util.Map;
import java.util.SortedSet;

/**
 * Represents an editor for an animation.
 */
public interface AnimationEditor extends Animation {

  /**
   * Inserts a keyFrame into this animation for the given Shape.
   *
   * @param shapeName name of shape to add keyFrame to
   * @param keyframe  keyframe to add
   */
  void insertKeyFrame(String shapeName, ReadOnlyKeyframe keyframe);

  /**
   * Remove keyFrame for the given shape at the given time.
   *
   * @param shapeName name of shape to remove keyFrame from
   * @param time      time the keyframe to be removed is at
   */
  void removeKeyFrame(String shapeName, int time);

  /**
   * Retrieves the data required for an animation editor.
   *
   * @return a map of shapes to their keyframes
   */
  Map<ReadOnlyShape, SortedSet<Keyframe>> getData();
}
