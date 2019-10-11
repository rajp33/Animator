package cs3500.animator.model;

import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A basic motion. This motion takes time, x, y, w, h, r, g, b for both start and end times.
 */
public class MotionImpl implements Motion {
  private static Map<List<Integer>, MotionImpl> allMotions;
  private Keyframe start;
  private Keyframe end;
  private List<MotionListener> subscribers;

  /**
   * Constructs a Motion.
   *
   * @param start    start time
   * @param end      end time
   * @param startPos starting position (x, y)
   * @param endPos   ending position (x,y)
   * @param startDim starting dimensions (w, h)
   * @param endDim   ending dimensions (w, h)
   * @param startCol starting color
   * @param endCol   ending color
   */
  private MotionImpl(int start, int end, Point.Double startPos, Point.Double endPos,
                     Point startDim, Point endDim, Color startCol, Color endCol) {
    if (end < start) {
      throw new IllegalArgumentException("Start time should be before end-time");
    }
    this.start = new Parameters(start, startDim.y, startDim.x, startPos, startCol);
    this.end = new Parameters(end, endDim.y, endDim.x, endPos, endCol);
    this.subscribers = new ArrayList<>();
  }

  private MotionImpl(Keyframe start, Keyframe end) {
    this.start = start;
    this.end = end;
  }

  /**
   * Creates a motion. Ensures that no duplicate motions are created.
   *
   * @param start    start-time
   * @param end      end-time
   * @param startPos start-positon (x, y)
   * @param endPos   end-position (x, y)
   * @param startDim start-dimensions
   * @param endDim   end-dimensions
   * @param startCol start-color
   * @param endCol   end-color
   * @return the Motion with the given parameters
   */
  public static Motion createMotion(int start, int end, Point.Double startPos, Point.Double endPos,
                                    Point startDim, Point endDim, Color startCol, Color endCol) {
    //if there were no motions made previously make a new map.
    if (allMotions == null) {
      allMotions = new HashMap<>();
    }
    //create a list with the given params
    ArrayList<Integer> paramList = new ArrayList<>(Arrays.asList(start, end, (int) startPos.x,
            (int) startPos.y, startDim.x, startDim.y, startCol.getRed(), startCol.getBlue(),
            startCol.getGreen(), (int) endPos.x, (int) endPos.y, endDim.x, endDim.y,
            endCol.getRed(), endCol.getBlue(), endCol.getGreen()));
    //if the map of motions already contains this list, return the motion already made
    if (allMotions.keySet().contains(paramList)) {
      return allMotions.get(paramList);
    } else {
      MotionImpl newMotion = new MotionImpl(start, end, startPos, endPos,
              startDim, endDim, startCol, endCol);
      allMotions.put(paramList, newMotion);
      return newMotion;
    }
  }

  /**
   * Create a unique motion with the given start and end keyFrames. If one already exists, it
   * returns it rather than creating a new one.
   *
   * @param start start keyframe
   * @param end   end keyframe
   * @return a unique motion
   */
  public static Motion createMotion(Keyframe start, Keyframe end) {
    if (allMotions == null) {
      allMotions = new HashMap<>();
    }
    Point2D.Double startPosition = start.getPosition();
    Color startColor = start.getColor();
    Color endColor = end.getColor();
    ArrayList<Integer> paramList = new ArrayList<>(Arrays.asList(
            start.getTime(), end.getTime(), (int) startPosition.x, (int) startPosition.y,
            (int) start.getWidth(), (int) start.getHeight(), startColor.getRed(),
            startColor.getBlue(), startColor.getGreen(), (int) end.getPosition().x,
            (int) end.getPosition().y, (int) end.getWidth(), (int) end.getHeight(),
            endColor.getRed(), endColor.getBlue(), endColor.getGreen()));
    if (allMotions.keySet().contains(paramList)) {
      return allMotions.get(paramList);
    } else {
      MotionImpl newMotion = new MotionImpl(start, end);
      allMotions.put(paramList, newMotion);
      return newMotion;
    }
  }


  @Override
  public Keyframe getStartParams() {
    return start;
  }

  @Override
  public Keyframe getEndParams() {
    return end;
  }

  @Override
  public void updateTo(int time) {
    if (time < 0) {
      throw new IllegalArgumentException("The time must be greater than 0");
    }
    for (MotionListener subscriber : this.subscribers) {
      Color startCol = start.getColor();
      Color endCol = end.getColor();
      Keyframe update = new Parameters(time,
              this.interpolate(time, start.getHeight(), end.getHeight()),
              this.interpolate(time, start.getWidth(), end.getWidth()),
              new Point.Double(this.interpolate(time, start.getPosition().x, end.getPosition().x),
                      this.interpolate(time, start.getPosition().y, start.getPosition().y)),
              new Color(this.interpolate(time, startCol.getRed(), endCol.getRed()),
                      this.interpolate(time, startCol.getGreen(), endCol.getGreen()),
                      this.interpolate(time, startCol.getBlue(), endCol.getBlue())));
      subscriber.updateRecieved(update);
    }
  }

  /**
   * Interpolates two values a and b to a certain given time.
   *
   * @param time time
   * @param a    first value
   * @param b    second value
   * @return the value interpolated to the time given
   */
  private int interpolate(int time, double a, double b) {
    int endTime = end.getTime();
    int startTime = start.getTime();
    return (int) Math.round((a * (((double) endTime - time) / (endTime - startTime))) +
            (b * (((double) time - startTime) / (endTime - startTime))));
  }

  @Override
  public void addSubscriber(MotionListener subscriber) {
    this.subscribers.add(subscriber);
  }

  @Override
  public List<MotionListener> getSubscribers() {
    return subscribers;
  }
}
