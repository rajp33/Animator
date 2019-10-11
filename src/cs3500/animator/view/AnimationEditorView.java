package cs3500.animator.view;

import java.util.Map;
import java.util.SortedSet;

import cs3500.animator.model.Keyframe;
import cs3500.animator.model.ReadOnlyShape;

/**
 * Represents a view for an Animation Editor. Supports displaying the animation:
 * - playing and pausing
 * - retstarting the animation
 * - enabling/disabling looping
 * - editing keyframes and creating/deleting shapes
 */
public interface AnimationEditorView extends AnimationView {

  /**
   * Sets the list of shapes used for rendering and displaying.
   * @param shapes shapes
   */
  void setData(Map<ReadOnlyShape, SortedSet<Keyframe>> shapes);

  /**
   * Adds a subscriber to listen to events and provide data.
   * @param subscriber subscriber
   */
  void addSubscriber(EditorFeatures subscriber);

  /**
   * Display the given time as the end-time of the animation.
   * @param time time in ticks
   */
  void setEndTime(int time);

  /**
   * Display the speed as the given speed.
   * @param speed speed (ticks/sec)
   */
  void setSpeed(int speed);

  /**
   * Display the given shape as selected.
   * @param shape shape to display as selected.
   */
  void setSelected(ReadOnlyShape shape);
}
