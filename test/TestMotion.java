import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;


import cs3500.animator.model.Ellipse;
import cs3500.animator.model.MotionImpl;
import cs3500.animator.model.Motion;
import cs3500.animator.model.MotionListener;
import cs3500.animator.model.Parameters;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.Shape;

/**
 * Tests for Motion.
 */
public class TestMotion {

  private static boolean initiated = false;
  private static Point.Double origin;
  private static Point.Double threeTwo;
  private static Motion changeColor;
  private static Motion changePosition;
  private static Motion changeAll;
  private static Motion changeColorOverlap;
  private static MotionListener ellipse;
  private static MotionListener rectangle;
  private static MotionListener rectangle2;

  /**
   * Method to init static vars.
   */
  @BeforeClass
  public static void init() {
    if (!initiated) {
      initiated = true;
      origin = new Point.Double(0.0, 0.0);
      threeTwo = new Point.Double(3.0, 2.0);
      Point fiveByFive = new Point(5, 5);
      Point oneByTwo = new Point(1, 2);
      changeColor = MotionImpl.createMotion(0, 10, origin, origin, fiveByFive,
              fiveByFive, Color.RED, Color.GREEN);
      changePosition = MotionImpl.createMotion(0, 10, origin, threeTwo, fiveByFive,
              fiveByFive, Color.RED, Color.RED);
      Motion changeSize = MotionImpl.createMotion(10, 20, threeTwo, threeTwo,
              fiveByFive, oneByTwo, Color.RED, Color.RED);
      changeAll = MotionImpl.createMotion(20, 40, origin, threeTwo, oneByTwo, fiveByFive,
              Color.RED, Color.GREEN);
      changeColorOverlap = MotionImpl.createMotion(9, 15, origin, origin, fiveByFive,
              fiveByFive, Color.RED, Color.GREEN);
      Motion changeSizeOverlap = MotionImpl.createMotion(10, 20, threeTwo, threeTwo,
              fiveByFive, oneByTwo, Color.RED, Color.RED);
      ellipse = new Ellipse(4, 3, 2, 2, 25, 25,
              10, "Ellipse Subscriber");
      rectangle = new Rectangle(2, 2, 10, 5, 25, 3, 2,
              "Rectangle subscriber");
      rectangle2 = new Rectangle(2, 2, 10, 5, 25, 3, 2,
              "Rectangle subscriber 2");
    }
  }

  //@Test
  //public void testUpdateTo() {
  //To be implemented in future assignments after we are given te tweaning function.
  //}

  @Test(expected = IllegalArgumentException.class)
  public void invalidMotion() {
    MotionImpl.createMotion(2, 1, new Point.Double(1, 1), new Point.Double(1, 1),
            new Point(3, 3), new Point(3, 3), Color.WHITE, Color.BLACK);
  }

  @Test
  public void testAddSubscriber() {
    changeColor.addSubscriber(rectangle);
    changeColor.addSubscriber(ellipse);
    changeColor.addSubscriber(rectangle2);
    changeColorOverlap.addSubscriber(rectangle);
    ArrayList<MotionListener> overlapSubs = new ArrayList<>();
    overlapSubs.add(rectangle);
    ArrayList<MotionListener> subs = new ArrayList<>();
    subs.add(rectangle);
    subs.add(ellipse);
    subs.add(rectangle2);
    assertEquals(subs, changeColor.getSubscribers());
    assertEquals(overlapSubs, changeColorOverlap.getSubscribers());
    assertEquals(new ArrayList<MotionListener>(), changePosition.getSubscribers());

  }

  @Test
  public void testGetStartParams() {
    Parameters startParamsChangeColor = new Parameters(0, 5, 5,
            origin, Color.RED);
    assertEquals(startParamsChangeColor, changeColor.getStartParams());
    Parameters startParamsChangeAll = new Parameters(20, 2,
            1, origin, Color.RED);
    assertEquals(startParamsChangeAll, changeAll.getStartParams());
  }

  @Test
  public void testGetEndParams() {
    Parameters endParamsChangeColor = new Parameters(10, 5, 5,
            origin, Color.GREEN);
    assertEquals(endParamsChangeColor, changeColor.getEndParams());
    Parameters endParamsChangeAll = new Parameters(40, 5,
            5, threeTwo, Color.GREEN);
    assertEquals(endParamsChangeAll, changeAll.getEndParams());
  }

  @Test(expected = IllegalArgumentException.class)
  public void updateToInvalidTime() {
    changeAll.updateTo(-1);
  }


  @Test
  public void testUpdateReceived() {
    Shape shape = new Ellipse(
            1, 1, 1, 1, 11, 1, 1, "fds;daf");
    shape.updateRecieved(new Parameters(2, 2, 2, new Point.Double(2, 2),
            Color.WHITE));
    assertEquals((int) shape.getHeight(), 2);
    assertEquals((int) shape.getWidth(), 2);
    assertEquals(shape.getPosition(), new Point.Double(2, 2));
    assertEquals(shape.getColor(), Color.WHITE);
  }
}
