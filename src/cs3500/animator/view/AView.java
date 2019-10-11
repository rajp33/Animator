package cs3500.animator.view;

import java.util.List;

import cs3500.animator.model.Animation;

/**
 * Represents a basic AnimationView. Contains abstracted code from concrete implementations.
 */
public abstract class AView implements AnimationView {
  Animation animation;
  int x;
  int y;
  int width;
  int height;
  int speed;

  /**
   * Constructs a ViewModel by reading from the animation to set bounds and by setting the
   * outputName. The outputName can be null if not used.
   *
   * @param animation  animation to read from
   * @param outputName name to give to output
   */
  public AView(Animation animation, String outputName) {
    this.read(animation);
  }

  @Override
  public void read(Animation animation) {

    List<Integer> bounds = animation.getCanvasParams();
    x = bounds.get(0);
    y = bounds.get(1);
    width = bounds.get(2);
    height = bounds.get(3);
    speed = animation.getSpeed();
    this.animation = animation;
  }
}