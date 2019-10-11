package cs3500.animator.model;

import java.awt.Color;
import java.awt.Point;
import java.util.Objects;

/**
 * Represents an object to transfer parameters.
 */
public class Parameters implements Keyframe {
  private int time;
  private double height;
  private double width;
  private Point.Double position;
  private Color color;

  /**
   * Constructs a Parameter.
   *
   * @param time     time
   * @param height   height
   * @param width    width
   * @param position position
   * @param color    color
   */
  public Parameters(int time, double height, double width,
                    Point.Double position, Color color) {
    this.time = time;
    this.color = color;
    this.height = height;
    this.width = width;
    this.position = position;
  }

  @Override
  public int getTime() {
    return time;
  }

  @Override
  public double getHeight() {
    return height;
  }

  @Override
  public double getWidth() {
    return width;
  }

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public Point.Double getPosition() {
    return position;
  }

  @Override
  public void setTime(int time) {
    this.time = time;
  }

  @Override
  public void setDimensions(double height, double width) {
    this.height = height;
    this.width = width;
  }

  @Override
  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public void setPosition(double x, double y) {
    this.position.x = x;
    this.position.y = y;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Parameters && ((Parameters) o).time == this.time
            && ((Parameters) o).color.equals(this.color)
            && ((Parameters) o).height == this.height
            && ((Parameters) o).width == this.width
            && ((Parameters) o).position.equals(this.position);
  }


  @Override
  public String toString() {
    return time + " " + (int) position.x + " " + (int) position.y +
            " " + (int) width + " " + (int) height + " " +
            color.getRed() + " " + color.getBlue() +
            " " + color.getGreen();
  }

  @Override
  public int compareTo(Keyframe o) {
    return Integer.compare(this.time, o.getTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, height, width, position, color);
  }
}