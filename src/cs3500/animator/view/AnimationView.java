package cs3500.animator.view;

import cs3500.animator.model.Animation;

/**
 * Represents a ViewModel for an Animation. The Model should be able to read from an animation,
 * refresh to a certain time, and render output.
 */
public interface AnimationView {

  /**
   * Read from an animation.
   *
   * @param animation animation to read from.
   */
  void read(Animation animation);

  /**
   * Render the animation.
   */
  void render();

  /**
   * Refresh the animation.
   *
   * @throws UnsupportedOperationException when refresh is called on a view that doesn't need to
   *                                       refresh.
   */
  void refresh(int time) throws UnsupportedOperationException;
}
