package cs3500.animator.view;

import java.util.Map;
import java.util.SortedSet;

import cs3500.animator.model.Keyframe;
import cs3500.animator.model.ReadOnlyKeyframe;
import cs3500.animator.model.ReadOnlyShape;

/**
 * Defines the features/functions that a controller for the AnimationEditor must be able to
 * perform.
 */
public interface EditorFeatures {

  /**
   * Sets the animation to playing.
   */
  void setPlaying();

  /**
   * Pauses the animation.
   */
  void setPaused();

  /**
   * Restarts the animation.
   */
  void restart();

  /**
   * Allows/disallows looping based on the boolean given.
   *
   * @param enabled is looping enabled
   */
  void setLooping(boolean enabled);

  /**
   * Creates a keyframe for the selected shape. Ensure that a shape is selected before this method
   * is run. Should refresh data to the view afterwards.
   *
   * @param parameters parameters for keyframe
   */
  void insertKeyFrame(ReadOnlyKeyframe parameters);

  //  /**
  //   *
  //   * @param shapeName name
  //   * @param parameters
  //   */
  //  void editKeyFrame(String shapeName, ReadOnlyKeyframe parameters);

  /**
   * Remove the keyframe from the selected shape. Ensure that a shape is selected before hand.
   *
   * @param time of keyframe to remove
   */
  void removeKeyFrame(int time);

  /**
   * Set the time to the given.
   *
   * @param frame time/frame of the animation (tick)
   */
  void setFrame(int frame);

  /**
   * Sets the speed of the animation to the given.
   *
   * @param speed (ticks/sec)
   */
  void setSpeed(int speed);

  /**
   * Removes the shape with the given name from the animation.
   *
   * @param name name of shape
   */
  void removeShape(String name);

  /**
   * Adds a shape to the animation with the given name and type.
   *
   * @param name name of shape
   * @param type type of shape
   */
  void addShape(String name, String type);

  /**
   * Retrieve data to be used in animation.
   *
   * @return data
   */
  Map<ReadOnlyShape, SortedSet<Keyframe>> getData();

  /**
   * Sets the shape with the given name as selected.
   *
   * @param name name of shpae
   */
  void setSelectedShape(String name);

  /**
   * Edit the Keyframe at the given time to update the parameter with the given name to the given
   * value.
   *
   * @param time     time
   * @param name     name of parameter to change
   * @param newValue value to set to
   */
  void editKeyframe(int time, String name, Object newValue);

  /**
   * Returns the number of frames rendered since the last call to this method.
   * This method should be called every second to get an accurate FPS.
   * @return FPS
   */
  double getFPS();
}
