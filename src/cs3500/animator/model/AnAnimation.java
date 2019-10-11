package cs3500.animator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a basic {@link Animation}. An Animation stores shapes and motions and handles applying
 * motions when the time is updated.
 */
public abstract class AnAnimation implements Animation {
  private final Map<Shape, List<Motion>> shapes;
  private int time;
  private int x;
  private int y;
  private int width;
  private int height;
  private int speed;
  int endTime;

  AnAnimation(int initialTime) {
    this.shapes = new LinkedHashMap<>();
    this.time = initialTime;
    this.x = 0; //default values
    this.y = 0; //dfault values
    this.width = 500; // default values
    this.height = 500; // default values
    this.speed = 1; //default values
    this.endTime = 0; //default values
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public void setSpeed(int speed) {
    this.speed = speed;
  }

  @Override
  public int getSpeed() {
    return this.speed;
  }

  @Override
  public List<Integer> getCanvasParams() {
    return new ArrayList<>(Arrays.asList(x, y, width, height));
  }

  @Override
  public List<ReadOnlyShape> getShapes() {
    return new ArrayList<>(this.shapes.keySet());
  }

  @Override
  public void removeShape(String name) throws IllegalArgumentException {
    boolean found = false;
    for (Shape shape : shapes.keySet()) {
      if (shape.getName().equals(name)) {
        shapes.remove(shape);
        found = true;
      }
    }
    if (!found) {
      throw new IllegalArgumentException("Shape with the name: " + name + " not found");
    }
  }

  @Override
  public void setTime(int time) {
    this.time = time;

    //for each motion in the shapes, find the motion that applies to this time and update shapes
    //accordingly
    for (List<Motion> loMotion : shapes.values()) {
      for (Motion motion : loMotion) {
        int start = motion.getStartParams().getTime();
        int end = motion.getEndParams().getTime();
        if (this.time >= start && this.time < end) {
          motion.updateTo(time);
        }
      }
    }
  }

  @Override
  public void addShape(Shape shape) throws IllegalArgumentException {
    if (shapes.containsKey(shape)) {
      throw new IllegalArgumentException("Shape already in the animation.");
    }
    this.shapes.put(shape, new ArrayList<>());
  }

  @Override
  public void addMotion(String shapeName, Motion motion) throws IllegalArgumentException {
    boolean subscribed = false;
    for (Shape shape : this.shapes.keySet()) {
      if (shape.getName().equals(shapeName)) {
        List<Motion> motions = this.shapes.get(shape);
        if (motions.size() > 0) {
          //check for time conflict
          Motion lastMotion = motions.get(motions.size() - 1);
          Keyframe existingEnd = lastMotion.getEndParams();
          Keyframe newStart = motion.getStartParams();
          if (!newStart.equals(existingEnd)) {
            throw new IllegalArgumentException("The motion that is being added must start in " +
                    "the same state as the existing motion: " + existingEnd);
          }
        }
        //make shape a subscriber to that motion
        motion.addSubscriber(shape);
        subscribed = true;
        //update map of shape to motions
        shapes.get(shape).add(motion);
        //update endtime
        this.endTime = Math.max(this.endTime, motion.getEndParams().getTime());
      }
    }
    if (!subscribed) {
      throw new IllegalArgumentException("Shape with name " + " not found.");
    }
  }

  @Override
  public int getEndTime() {
    return this.endTime;
  }

  @Override
  public String status() {
    StringBuilder output = new StringBuilder();
    for (Shape shape : this.shapes.keySet()) {
      output.append("shape ").append(shape.getName())
              .append(" ").append(shape.getType()).append("\n");
      List<Motion> motions = this.shapes.get(shape);
      for (Motion motion : motions) {
        Keyframe start = motion.getStartParams();
        Keyframe end = motion.getEndParams();
        output.append("motion ").append(shape.getName()).append(" ").append(start.toString())
                .append(" ").append(end.toString());
        output.append("\n");
      }
    }
    return output.toString();
  }
}
