package cs3500.animator.model;

import java.awt.Color;
import java.awt.Point;

import cs3500.animator.util.AnimationBuilder;

/**
 * Represents an implementation of an animation.
 */
public class AnimationImpl extends AnAnimation {

  /**
   * Constructs an Animation with the given initialTime.
   *
   * @param initialTime initial time
   */
  public AnimationImpl(int initialTime) {
    super(initialTime);
  }

  /**
   * Builder for an AnimationImpl.
   */
  public static final class Builder implements AnimationBuilder<Animation> {
    final Animation animation = new AnimationImpl(0);

    @Override
    public Animation build() {
      return animation;
    }

    @Override
    public AnimationBuilder<Animation> setBounds(int x, int y, int width, int height) {
      animation.setBounds(x, y, width, height);
      return this;
    }

    @Override
    public AnimationBuilder<Animation> declareShape(String name, String type) {
      animation.addShape(ShapeFactory.createShape(name, type));
      return this;
    }

    @Override
    public AnimationBuilder<Animation> addMotion(String name, int t1, int x1, int y1, int w1,
                                                 int h1, int r1, int g1, int b1, int t2,
                                                 int x2, int y2, int w2, int h2, int r2,
                                                 int g2, int b2) {
      animation.addMotion(name, MotionImpl.createMotion(t1, t2, new Point.Double(x1, y1),
              new Point.Double(x2, y2), new Point(w1, h1), new Point(w2, h2), new Color(r1, g1, b1),
              new Color(r2, g2, b2)));
      return this;
    }

    @Override
    public AnimationBuilder<Animation> addKeyframe(String name, int t, int x, int y, int w,
                                                   int h, int r, int g, int b) {
      return null;
    }
  }
}
