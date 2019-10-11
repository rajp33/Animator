package cs3500.animator.model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a basic {@link Shape}.
 */
public abstract class AShape implements Shape {
  private static List<String> names;
  private double height;
  private double width;
  private Point.Double position;
  private Color color;
  private final String name;

  /**
   * Constructs a basic AShape. Checks for uniqueness of names across AShapes.
   *
   * @param height   height of shape
   * @param width    width of shape
   * @param position position of shape
   * @param color    color of shape
   * @param name     name of shape
   * @throws IllegalArgumentException if name is not unique
   */
  AShape(int height, int width, Point.Double position, Color color, String name)
          throws IllegalArgumentException {
    if (names == null) {
      names = new ArrayList<>();
    }
    if (!isNameUnique(name)) {
      throw new IllegalArgumentException("Shape name " + name + " is not unique.");
    }
    this.height = height;
    this.width = width;
    this.position = position;
    this.color = color;
    this.name = name;
    names.add(name);
  }

  @Override
  public void updateRecieved(Keyframe parameters) {
    this.color = parameters.getColor();
    this.position = parameters.getPosition();
    this.width = parameters.getWidth();
    this.height = parameters.getHeight();
  }

  /**
   * Checks if the given name is unique compared to all AShapes.
   *
   * @param name name to check
   * @return true if name is unique
   */
  private static boolean isNameUnique(String name) {
    return !names.contains(name);
  }

  @Override
  public double getHeight() {
    return this.height;
  }

  @Override
  public double getWidth() {
    return this.width;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public Point.Double getPosition() {
    return this.position;
  }

  @Override
  public void setHeight(double h) {
    this.height = h;
  }

  @Override
  public void setWidth(double w) {
    this.width = w;
  }

  @Override
  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public void setPosition(Point.Double newPosition) {
    this.position = newPosition;
  }

  @Override
  public String getName() {
    return this.name;
  }
}
