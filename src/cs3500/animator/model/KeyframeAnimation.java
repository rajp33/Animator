package cs3500.animator.model;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import cs3500.animator.util.AnimationBuilder;

/**
 * Represents an {@link Animation} model that works with Keyframes instead of Motions.
 */
public class KeyframeAnimation extends AnAnimation implements AnimationEditor {
  protected Map<Shape, SortedSet<Keyframe>> shapes;
  private int time;

  /**
   * Constructs an instance of the KeyframeAnimation.
   */
  public KeyframeAnimation() {
    super(0);
    this.shapes = new LinkedHashMap<>();
  }


  @Override
  public void insertKeyFrame(String shapeName, ReadOnlyKeyframe keyframe) {
    //find shape to insert keyframe into
    for (Shape shape : this.shapes.keySet()) {
      if (shape.getName().equals(shapeName)) {
        SortedSet<Keyframe> keyframes = this.shapes.get(shape);
        List<Keyframe> keyframeList = new ArrayList<>(keyframes);

        for (Keyframe checkFrame : keyframeList) {
          if (checkFrame.getTime() == keyframe.getTime()) {
            keyframes.remove(checkFrame);
          }
        }

        //add keyframe
        keyframes.add(new Parameters(keyframe.getTime(), keyframe.getHeight(), keyframe.getWidth(),
            keyframe.getPosition(), keyframe.getColor()));

        //update endtime
        this.endTime = Math.max(keyframe.getTime(), this.endTime);
      }
    }
  }

  @Override
  public void removeKeyFrame(String shapeName, int time) {
    boolean removed = false;
    for (Shape shape : this.shapes.keySet()) {
      if (shape.getName().equals(shapeName)) {
        SortedSet<Keyframe> set = this.shapes.get(shape);
        List<Keyframe> keyframeList = new ArrayList<>(set);
        for (int i = 0; i < keyframeList.size() && !removed; i++) {
          Keyframe checkFrame = keyframeList.get(i);
          if (checkFrame.getTime() == time) {
            set.remove(checkFrame);
            removed = true;
          }
        }
      }
    }
    if (!removed) {
      throw new IllegalArgumentException("Shape with name: " + shapeName + " does not exist");
    }
  }

  @Override
  public List<ReadOnlyShape> getShapes() {
    return new ArrayList<>(this.shapes.keySet());
  }

  @Override
  public void setTime(int time) {
    this.time = time;
    //for each shape
    for (Shape shape : this.shapes.keySet()) {
      List<Keyframe> keyframes = new ArrayList<>(this.shapes.get(shape));

      //if there is at least one keyframe
      if (keyframes.size() > 0) {
        Keyframe first = keyframes.get(0);

        //if the time is equal to the first then update to first
        if (time == first.getTime()) {
          shape.updateRecieved(first);
          //else if time is greater than last or less than first then become blank
        } else if (time >= keyframes.get(keyframes.size() - 1).getTime()
            || time < first.getTime()) {
          shape.updateRecieved(new Parameters(time, 0, 0,
              new Point.Double(0, 0), Color.WHITE));
        } else {
          boolean updated = false;

          //interpolate between keyframes
          for (int i = 0; i < keyframes.size() - 1 && !updated; i++) {
            Keyframe keyframeBefore = keyframes.get(i);
            Keyframe keyframeAfter = keyframes.get(i + 1);
            if (time < keyframeAfter.getTime() && time >= keyframeBefore.getTime()) {
              shape.updateRecieved(this.interpolate(keyframeBefore, keyframeAfter));
              updated = true;
            }
          }
        }
      }
    }
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
  public void addMotion(String shapeName, Motion motion) throws IllegalArgumentException {
    boolean subscribed = false;

    // find shape to add motion to
    for (Shape shape : this.shapes.keySet()) {
      if (shape.getName().equals(shapeName)) {
        SortedSet<Keyframe> keyframes = this.shapes.get(shape);

        //check to make sure last keyframe is the same as beginning of this motion
        if (keyframes.size() > 0 && !keyframes.last().equals(motion.getStartParams())) {
          throw new IllegalArgumentException("Motions must be successive (must start in the same"
              + "state that the last motion ended in");
        }

        //add motion as keyframe
        Keyframe end = motion.getEndParams();
        keyframes.add(end);
        subscribed = true;

        //update endtime
        this.endTime = Math.max(end.getTime(), this.endTime);
      }
    }
    if (!subscribed) {
      throw new IllegalArgumentException("Shape with name: " + shapeName + "does not exist");
    }
  }

  @Override
  public String status() {
    StringBuilder output = new StringBuilder();
    for (Shape shape : this.shapes.keySet()) {
      output.append("shape ").append(shape.getName())
          .append(" ").append(shape.getType()).append("\n");
      List<Keyframe> keyframes = new ArrayList<>(this.shapes.get(shape));

      for (int i = 0; i < keyframes.size() - 1; i++) {
        Keyframe start = keyframes.get(i);
        Keyframe end = keyframes.get(i + 1);
        output.append("motion ").append(shape.getName()).append(" ").append(start.toString())
            .append(" ").append(end.toString());
        output.append("\n");
      }
    }
    return output.toString();
  }

  @Override
  public void addShape(Shape shape) throws IllegalArgumentException {
    if (shapes.containsKey(shape)) {
      throw new IllegalArgumentException("Shape already exists");
    } else {
      this.shapes.put(shape, new TreeSet<>());
    }
  }

  @Override
  public void removeShape(String name) throws IllegalArgumentException {
    Shape toRemove = null;
    for (Shape shape : shapes.keySet()) {
      if (shape.getName().equals(name)) {
        toRemove = shape;
      }
    }
    if (toRemove == null) {
      throw new IllegalArgumentException("Shape with name: " + name + " not found");
    }
    SortedSet<Keyframe> removed = this.shapes.remove(toRemove);

    if (removed.size() > 0 && this.endTime == removed.last().getTime()) {
      this.endTime = this.calculateEndtime();
    }
  }

  /**
   * Calculates the endTime based on the last keyframe in this animation.
   * @return tick value of last keyframe
   */
  private int calculateEndtime() {
    int end = this.endTime;
    for (Shape shape : this.shapes.keySet()) {
      SortedSet<Keyframe> keyframes = this.shapes.get(shape);
      for (Keyframe keyframe : keyframes) {
        end = Math.max(keyframe.getTime(), end);
      }
    }
    return end;
  }

  @Override
  public Map<ReadOnlyShape, SortedSet<Keyframe>> getData() {
    LinkedHashMap<ReadOnlyShape, SortedSet<Keyframe>> data = new LinkedHashMap<>();
    for (Shape shape : this.shapes.keySet()) {
      data.put(shape, this.shapes.get(shape));
    }
    return data;
  }

  /**
   * Builder for an AnimationEditor.
   */
  public final static class Builder implements AnimationBuilder<AnimationEditor> {
    AnimationEditor animation = new KeyframeAnimation();

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
