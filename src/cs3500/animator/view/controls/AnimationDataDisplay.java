package cs3500.animator.view.controls;

import java.util.Map;
import java.util.SortedSet;

import cs3500.animator.model.Keyframe;
import cs3500.animator.model.ReadOnlyShape;
import cs3500.animator.view.EditorFeatures;

/**
 * Represents an object that will display shape/keyframe data.
 */
public interface AnimationDataDisplay {

  /**
   * Set the data to display.
   *
   * @param data data
   */
  void setData(Map<ReadOnlyShape, SortedSet<Keyframe>> data);

  /**
   * Adds a controller.
   *
   * @param controller controller.
   */
  void addController(EditorFeatures controller);

  /**
   * Sets the given shape to be selected.
   *
   * @param shape shape
   */
  void setSelected(ReadOnlyShape shape);
}
