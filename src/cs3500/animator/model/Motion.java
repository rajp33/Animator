package cs3500.animator.model;

import java.util.List;

/**
 * Represents an action that a Shape can undergo from a specific start time to an end-time.
 */
public interface Motion {

  /**
   * Return the start-parameters of this motion.
   *
   * @return start-parameters
   */
  Keyframe getStartParams();

  /**
   * Return the end-parameters of this motion.
   *
   * @return end-parameters
   */
  Keyframe getEndParams();

  /**
   * Modifies this motion's subscribers to reflect their states at the given time.
   *
   * @param time the time to update to
   */
  void updateTo(int time) throws IllegalArgumentException;

  /**
   * Add a MotionListener to the list of subscribers of this motion.
   */
  void addSubscriber(MotionListener subscriber);

  /**
   * Gets the subscribers to this motion.
   */
  List<MotionListener> getSubscribers();
}
