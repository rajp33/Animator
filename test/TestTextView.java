import org.junit.Test;

import java.awt.Color;
import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import cs3500.animator.model.Animation;
import cs3500.animator.model.AnimationImpl;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.Motion;
import cs3500.animator.model.MotionImpl;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.Shape;
import cs3500.animator.view.AnimationView;
import cs3500.animator.view.ViewFactory;

import static junit.framework.TestCase.assertEquals;

/**
 * Represents tests for the Textual view of the Animaator.
 */
public class TestTextView {
  private static Animation emptyAnimation;
  private static Animation animation;
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
  private static AnimationView testView;
  private static AnimationView emptyView;

  private static void init() {
    animation = new AnimationImpl(0);
    emptyAnimation = new AnimationImpl(0);
    testView = ViewFactory.create("text", animation, null);
    emptyView = ViewFactory.create("text", emptyAnimation, null);
    animation.addShape(shape);
    animation.addShape(shape2);
    animation.addShape(hape);
    animation.addMotion(shape.getName(), changeColor);
    animation.addMotion(shape2.getName(), changePosition);
    animation.addMotion(shape.getName(), changeSize);
    animation.addMotion(shape2.getName(), changeSize2);
    animation.addMotion(shape.getName(), changeAll);
  }


  @Test(expected = UnsupportedOperationException.class)
  public void TestCallingRefresh() {
    init();
    testView.refresh(1);
  }

  @Test
  public void testRender() {
    init();
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    PrintStream newStream = new PrintStream(stream);
    PrintStream original = System.out;
    System.setOut(newStream);
    testView.render();
    System.out.flush();
    System.setOut(original);
    assertEquals(stream.toString(),
            "canvas 0 0 500 500\n" +
                    "shape r rectangle\n" +
                    "motion r 0 0 0 5 5 255 0 0 10 0 0 5 5 0 0 255\n" +
                    "motion r 10 0 0 5 5 0 0 255 20 0 0 1 2 0 0 255\n" +
                    "motion r 20 0 0 1 2 0 0 255 40 3 2 5 5 255 0 0\n" +
                    "shape e ellipse\n" +
                    "motion e 0 0 0 5 5 255 0 0 10 3 2 5 5 255 0 0\n" +
                    "motion e 10 3 2 5 5 255 0 0 20 0 0 1 2 0 0 255\n" +
                    "shape Dave ellipse\n\n");
  }

  @Test
  public void testRenderEmpty() {
    init();
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    PrintStream newStream = new PrintStream(stream);
    PrintStream original = System.out;
    System.setOut(newStream);
    emptyView.render();
    System.out.flush();
    System.setOut(original);
    assertEquals(stream.toString(),
            "canvas 0 0 500 500\n\n");
  }

  @Test
  public void testRead() {
    init();
    emptyView.read(animation);
    testView.read(emptyAnimation);
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    PrintStream newStream = new PrintStream(stream);
    PrintStream original = System.out;
    System.setOut(newStream);
    emptyView.render();
    System.out.flush();
    System.setOut(original);
    assertEquals(stream.toString(),
            "canvas 0 0 500 500\n" +
                    "shape r rectangle\n" +
                    "motion r 0 0 0 5 5 255 0 0 10 0 0 5 5 0 0 255\n" +
                    "motion r 10 0 0 5 5 0 0 255 20 0 0 1 2 0 0 255\n" +
                    "motion r 20 0 0 1 2 0 0 255 40 3 2 5 5 255 0 0\n" +
                    "shape e ellipse\n" +
                    "motion e 0 0 0 5 5 255 0 0 10 3 2 5 5 255 0 0\n" +
                    "motion e 10 3 2 5 5 255 0 0 20 0 0 1 2 0 0 255\n" +
                    "shape Dave ellipse\n\n");
    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
    PrintStream newStream2 = new PrintStream(stream);
    System.setOut(newStream2);
    testView.render();
    System.out.flush();
    System.setOut(original);
    assertEquals(stream2.toString(),
            "canvas 0 0 500 500\n\n");
  }
}
