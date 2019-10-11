package cs3500.animator.model;


/**
 * Represents an object that subscribes to a Motion. Requires a motion to subscribe to.
 */
public interface MotionListener {

  /**
   * Updates the Listener with given Parameters. To subscribe to a Motion, use {@link
   * Motion#addSubscriber(MotionListener)}.
   */
  void updateRecieved(Keyframe parameters);

}
