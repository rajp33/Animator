package cs3500.animator.model.threadableAnimation;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cs3500.animator.model.AnimationEditor;
import cs3500.animator.model.Keyframe;
import cs3500.animator.model.KeyframeAnimation;
import cs3500.animator.model.MotionImpl;
import cs3500.animator.model.Parameters;
import cs3500.animator.model.Shape;
import cs3500.animator.model.ShapeFactory;
import cs3500.animator.util.AnimationBuilder;

/**
 * Represents an animtation that is multi-threaded.
 */
public class ThreadableAnimation extends KeyframeAnimation {
  private final ExecutorService executor;


  private ThreadableAnimation() {
    super();
    int numCores = Runtime.getRuntime().availableProcessors();
    System.out.println("Number of cores: " + numCores);
    this.executor = Executors.newFixedThreadPool(numCores);
  }

  @Override
  public void setTime(int time) {
    for (Shape shape : this.shapes.keySet()) {
      executor.execute(new RunSetTime(shape, this.shapes.get(shape), time));
    }
  }

  /**
   * Represents a runnable object to set the time and update shapes.
   */
  private class RunSetTime implements Runnable {
    Shape shape;
    int time;
    List<Keyframe> frames;

    RunSetTime(Shape shape, SortedSet<Keyframe> frames, int time) {
      this.shape = shape;
      this.frames = new ArrayList<>(frames);
      this.time = time;
    }

    /**
     * Interpolates two keyframes. Uses current animation time.
     * @param a first keyframe
     * @param b second keyframe
     * @return a keyframe interpolated to the time given.
     */
    private Keyframe interpolate(Keyframe a, Keyframe b) {
      int endTime = b.getTime();
      int startTime = a.getTime();
      Color startCol = a.getColor();
      Color endCol = b.getColor();
      return new Parameters(time,
          this.interpolateHelper(a.getHeight(), b.getHeight(), startTime, endTime),
          this.interpolateHelper(a.getWidth(), b.getWidth(), startTime, endTime),
          new Point.Double(
              this.interpolateHelper(a.getPosition().x, b.getPosition().x, startTime, endTime),
              this.interpolateHelper(a.getPosition().y, b.getPosition().y, startTime, endTime)),
          new Color(
              this.interpolateHelper(startCol.getRed(), endCol.getRed(), startTime, endTime),
              this.interpolateHelper(startCol.getGreen(), endCol.getGreen(), startTime, endTime),
              this.interpolateHelper(startCol.getBlue(), endCol.getBlue(), startTime, endTime)));
    }

    /**
     * Interpolates two values. Uses current time of animation to determine what to interpolate to
     * @param a starting value
     * @param b ending value
     * @param startTime starting time
     * @param endTime ending time
     * @return value interpolated to the current time in the animation.
     */
    private int interpolateHelper(double a, double b, int startTime, int endTime) {
      return (int) Math.round((a * (((double) endTime - time) / (endTime - startTime))) +
          (b * (((double) time - startTime) / (endTime - startTime))));
    }

    @Override
    public void run() {
      //if there is at least one keyframe
      if (frames.size() > 0) {
        Keyframe first = frames.get(0);

        //if the time is equal to the first then update to first
        if (time == first.getTime()) {
          shape.updateRecieved(first);
          //else if time is greater than last or less than first then become blank
        } else if (time >= frames.get(frames.size() - 1).getTime()
            || time < first.getTime()) {
          shape.updateRecieved(new Parameters(time, 0, 0,
              new Point.Double(0, 0), Color.WHITE));
        } else {
          boolean updated = false;

          //interpolate between keyframes
          for (int i = 0; i < frames.size() - 1 && !updated; i++) {
            Keyframe keyframeBefore = frames.get(i);
            Keyframe keyframeAfter = frames.get(i + 1);
            if (time < keyframeAfter.getTime() && time >= keyframeBefore.getTime()) {
              shape.updateRecieved(interpolate(keyframeBefore, keyframeAfter));
              updated = true;
            }
          }
        }
      }
    }
  }
  /**
   * Builder for an AnimationEditor.
   */
  public static final class TABuilder implements AnimationBuilder<AnimationEditor> {
    AnimationEditor animation = new ThreadableAnimation();

    @Override
    public AnimationEditor build() {
      return animation;
    }

    @Override
    public AnimationBuilder<AnimationEditor> setBounds(int x, int y, int width, int height) {
      animation.setBounds(x, y, width, height);
      return this;
    }

    @Override
    public AnimationBuilder<AnimationEditor> declareShape(String name, String type) {
      animation.addShape(ShapeFactory.createShape(name, type));
      return this;
    }

    @Override
    public AnimationBuilder<AnimationEditor> addMotion(String name, int t1, int x1, int y1, int w1,
                                                       int h1, int r1, int g1, int b1, int t2, int x2,
                                                       int y2, int w2, int h2, int r2, int g2, int b2) {
      animation.addMotion(name, MotionImpl.createMotion(t1, t2, new Point.Double(x1, y1),
          new Point2D.Double(x2, y2), new Point(w1, h1), new Point(w2, h2),
          new Color(r1, g1, b1), new Color(r2, g2, b2)));
      return this;
    }

    @Override
    public AnimationBuilder<AnimationEditor> addKeyframe(String name,
                                                         int t, int x, int y, int w, int h,
                                                         int r, int g, int b) {
      animation.insertKeyFrame(name, new Parameters(t, h, w,
          new Point.Double(x, y), new Color(r, g, b)));
      return this;
    }
  }
}
