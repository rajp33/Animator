import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import cs3500.animator.model.Animation;
import cs3500.animator.model.AnimationImpl;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.Motion;
import cs3500.animator.model.MotionImpl;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.Shape;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Tests for Animation.
 */
public class AnimationImplTest {
  private static final Animation animation = new AnimationImpl(0);
  private static final Shape shape = new Rectangle(1, 1, 1, 1, 1, 1,
          1, "r");
  private static final Shape shape2 = new Ellipse(1, 1, 1, 1, 1,
          1, 1, "e");
  private static final Shape hape = new Ellipse(1, 1, 1, 1, 1, 1,
          1, "Dave");

  private static final Point.Double threeTwo = new Point.Double(3.0, 2.0);
  private static final Point.Double origin = new Point.Double(0.0, 0.0);
  private static final Point fiveByFive = new Point(5, 5);
  private static final Point oneByTwo = new Point(1, 2);
  private static final Point.Double fiveFive = new Point.Double(5.0, 5.0);


  private static final Motion changePosition = MotionImpl.createMotion(0, 10, origin,
          threeTwo, fiveByFive, fiveByFive, Color.RED, Color.RED);
  private static final Motion changeColor = MotionImpl.createMotion(0, 10, origin,
          origin, fiveByFive, fiveByFive, Color.RED, Color.GREEN);
  private static final Motion changeSize = MotionImpl.createMotion(10, 20, origin, origin,
          fiveByFive, oneByTwo, Color.GREEN, Color.GREEN);
  private static final Motion changeSize2 = MotionImpl.createMotion(10, 20, threeTwo, origin,
          fiveByFive, oneByTwo, Color.RED, Color.GREEN);
  private static final Motion changeAll = MotionImpl.createMotion(20, 40, origin, threeTwo,
          oneByTwo, fiveByFive, Color.GREEN, Color.RED);
  private static final Motion changeColorOverlap = MotionImpl.createMotion(9, 15, origin,
          origin, fiveByFive, fiveByFive, Color.RED, Color.GREEN);
  private static final Motion changeSizeOverlap = MotionImpl.createMotion(10, 20, threeTwo,
          threeTwo, fiveByFive, oneByTwo, Color.RED, Color.RED);
  private static final Motion changePositionOverlap = MotionImpl.createMotion(0, 10, origin,
          fiveFive, fiveByFive, fiveByFive, Color.RED, Color.RED);


  @BeforeClass
  public static void setUp() {
    animation.addShape(shape);
    animation.addShape(shape2);
  }


  @Test
  public void getShapes() {
    assertTrue(animation.getShapes().containsAll(
            new ArrayList<>(Arrays.asList(shape, shape2))));
  }

  //@Test
  //public void setTime() {
  //animation.setTime(1);
  //CANNOT TEST THIS BECAUSE THERE IS NO TWEENING FUNCTION TO UPDATE SHAPES AND THE MOTION's
  // UPDATETO method still has to be implemented
  //}

  @Test
  public void addShape() {
    animation.addShape(hape);
    assertTrue(animation.getShapes().containsAll(
            new ArrayList<>(Arrays.asList(shape, shape2, hape))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void addAlreadyAddedShape() {
    animation.addShape(shape);
    animation.addShape(shape2);
    animation.addShape(shape);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddingOverlappingColor() {
    animation.addMotion(shape.getName(), changeColor);
    animation.addMotion(shape.getName(), changeColorOverlap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddingOverlappingSize() {
    animation.addMotion(shape.getName(), changeSize);
    animation.addMotion(shape.getName(), changeSizeOverlap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddingOverlappingMove() {
    animation.addMotion(shape.getName(), changePosition);
    animation.addMotion(shape.getName(), changePositionOverlap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMotionGap() {
    Animation animation2 = new AnimationImpl(0);
    animation2.addShape(shape);
    animation2.addMotion("r", MotionImpl.createMotion(0, 10,
            new Point2D.Double(10, 10), new Point2D.Double(20, 20),
            new Point(10, 10), new Point(20, 20), Color.BLACK, Color.WHITE));
    animation2.addMotion("r", MotionImpl.createMotion(11, 22,
            new Point2D.Double(20, 20), new Point2D.Double(10, 10),
            new Point(20, 20), new Point(10, 10), Color.WHITE, Color.BLACK));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDiffState() {
    Animation animation3 = new AnimationImpl(0);
    animation3.addShape(shape);
    animation3.addMotion("r", MotionImpl.createMotion(0, 10,
            new Point2D.Double(10, 10), new Point2D.Double(20, 20),
            new Point(10, 10), new Point(20, 20), Color.BLACK, Color.WHITE));
    animation3.addMotion("r", MotionImpl.createMotion(10, 22,
            new Point2D.Double(10, 20), new Point2D.Double(10, 10),
            new Point(20, 20), new Point(10, 10), Color.WHITE, Color.BLACK));
  }

  @Test
  public void testAddingToAnimation() {
    assertEquals(animation.status(), "shape e ellipse\n" +
            "shape r rectangle\n" +
            "shape Dave ellipse\n");
    animation.addMotion(shape.getName(), changeColor);
    animation.addMotion(shape2.getName(), changePosition);
    assertEquals(animation.status(),
            "shape e ellipse\n" +
                    "motion e 0 0 0 5 5 255 0 0 10 3 2 5 5 255 0 0\n" +
                    "shape r rectangle\n" +
                    "motion r 0 0 0 5 5 255 0 0 10 0 0 5 5 0 0 255\n" +
                    "shape Dave ellipse\n");
    animation.addMotion(shape.getName(), changeSize);
    animation.addMotion(shape2.getName(), changeSize2);
    animation.addMotion(shape.getName(), changeAll);
    assertEquals(animation.status(),
            "shape e ellipse\n" +
                    "motion e 0 0 0 5 5 255 0 0 10 3 2 5 5 255 0 0\n" +
                    "motion e 10 3 2 5 5 255 0 0 20 0 0 1 2 0 0 255\n" +
                    "shape r rectangle\n" +
                    "motion r 0 0 0 5 5 255 0 0 10 0 0 5 5 0 0 255\n" +
                    "motion r 10 0 0 5 5 0 0 255 20 0 0 1 2 0 0 255\n" +
                    "motion r 20 0 0 1 2 0 0 255 40 3 2 5 5 255 0 0\n" +
                    "shape Dave ellipse\n");
  }

  @Test
  public void testStatusEmpty() {
    Animation emptyAnimation = new AnimationImpl(0);
    assertEquals(emptyAnimation.status(), "");
  }

  //TODO: Tests for new methods
  //TODO: tests for factories
}