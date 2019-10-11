package cs3500.animator.model;

import java.util.List;

/**
 * Represents an animation. An animation will have a list of commands and every tick it will call
 * tick on the commands - time needs to be b/w start and end for it to perform command
 */
public interface Animation {

  /**
   * Sets the bounds for the animation.
   *
   * @param x      The leftmost x value
   * @param y      The topmost y value
   * @param width  The width of the bounding box
   * @param height The height of the bounding box
   */
  void setBounds(int x, int y, int width, int height);

  /**
   * Sets the speed of the animation.
   *
   * @param speed speed of the animation (ticks/sec)
   */
  void setSpeed(int speed);

  /**
   * Returns the speed of the animation.
   *
   * @return speed
   */
  int getSpeed();

  /**
   * Returns the bounds of this animation as a List of integers.
   *
   * @return    a list of bounds, the first being the leftmost x value, the second the
   *            topmost y value, the third the width of the bounding box,
   *            the fourth the height of the bounding box.
   */
  List<Integer> getCanvasParams();

  /**
   * Returns the shapes that are in the animation at the time that the Animation is at. Should only
   * be called once for best performance. To change time and update shapes, use {@link
   * #setTime(int)}. Ensure that this method passes references of the shapes, not a copy so that
   * when the shapes are mutated, this method does not have to be called again and again.
   *
   * @return a list of shapes
   */
  List<ReadOnlyShape> getShapes();

  /**
   * Sets the time of the animation. This method should update the Shapes in this Animation to
   * represent their forms at the given time.
   *
   * @param time time in unitless ticks
   */
  void setTime(int time);


  /**
   * Add a {@link Shape} to the animation.
   *
   * @param shape the shape
   * @throws IllegalArgumentException when the shape already exists in the animation.
   */
  void addShape(Shape shape) throws IllegalArgumentException;

  /**
   * Remove a {@link Shape} with the given name.
   *
   * @param name name of the shape
   * @throws IllegalArgumentException when the shape with the given name is not found.
   */
  void removeShape(String name) throws IllegalArgumentException;


  /**
   * Adds a motion to this animation and subscribes the shape with the given shapeName to its
   * subscribers. If the motion already exists, simply add the shape to the subscribers using {@link
   * Motion#addSubscriber(MotionListener)};
   *
   * @param    shapeName name of shape to add to this motion
   * @param     motion    the Motion to add to this animation.
   * @throws    IllegalArgumentException when the shape with the given name doesn't exist.
   */
  void addMotion(String shapeName, Motion motion) throws IllegalArgumentException;

  /**
   * Returns a String representation of this animation. Should: 1. Declare a shape 2. Describe the
   * motions of the shape between two moments in this format: # declares a rectangle shape named R
   * shape R rectangle # describes the motions of shape R, between two moments of animation: # t ==
   * tick # (x,y) == position # (w,h) == dimensions # (r,g,b) == color (with values between 0 and
   * 255) #                  start end #        ---------------------------------------------------
   * #        t  x   y w  h   r   g  b    t   x   y   w  h   r   g  b motion R 1  200 200 50 100 255
   * 0  0    10  200 200 50 100 255 0  0 motion R 10 200 200 50 100 255 0  0    50  300 300 50 100
   * 255 0  0 motion R 50 300 300 50 100 255 0  0    51  300 300 50 100 255 0  0 motion R 51 300 300
   * 50 100 255 0  0 70  300 300 25 100 255 0  0 motion R 70 300 300 25 100 255 0  0    100 200 200
   * 25 100 255 0 0 shape C ellipse motion C 6  440 70 120 60 0 0 255      20  440 70  120 60 0 0
   * 255 motion C 20 440 70 120 60 0 0 255      50  440 250 120 60 0 0 255 motion C 50 440 250 120
   * 60 0 0 255     70 440 370 120 60 0 170 85 motion C 70 440 370 120 60 0 170 85    80  440 370
   * 120 60 0 255  0 motion C 80 440 370 120 60 0 255  0    100 440 370 120 60 0 255  0
   *
   * @return string status of animation
   */
  String status();

  /**
   * Returns the end-time of the animation. Should be calculated based on the end-time of the last
   * motion.
   *
   * @return end-time of Animation.
   */
  int getEndTime();
}
