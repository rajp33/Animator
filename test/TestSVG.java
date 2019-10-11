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
 * Represents tests for the SVGanimationView.
 */
public class TestSVG {
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
    testView = ViewFactory.create("svg", animation, null);
    emptyView = ViewFactory.create("svg", emptyAnimation, null);
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
                    "shape Dave ellipse\n" +
                    "\n" +
                    "<svg width=\"500\" height=\"500\" version=\"1.1\" " +
                    "xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "<rect id=\"r\" x=\"0\" y=\"0\" width=\"0\" height=\"0\" fill=\"rgb(0,0,0)" +
                    "\" visibility=\"hidden\">\n" +
                    "<set attributeName=\"visibility\" to=\"visible\" begin=\"0ms\"" +
                    " dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"x\" to=\"0\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"y\" to=\"0\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"height\" to=\"5\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"width\" to=\"5\" begin=\"0ms\" dur=\"0.0ms\"" +
                    " fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" end=\"10000ms\" " +
                    "attributeName=\"fill\" from=\"rgb(255,0,0)\" to=\"rgb(0,0,255)\" " +
                    "fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"height\" from=\"5\" to=\"2\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"width\" from=\"5\" to=\"1\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"20000ms\" end=\"40000ms\" " +
                    "attributeName=\"x\" from=\"0\" to=\"3\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"20000ms\" end=\"40000ms\" " +
                    "attributeName=\"y\" from=\"0\" to=\"2\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"20000ms\" end=\"40000ms\"" +
                    " attributeName=\"height\" from=\"2\" to=\"5\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"20000ms\" end=\"40000ms\"" +
                    " attributeName=\"width\" from=\"1\" to=\"5\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"20000ms\" end=\"40000ms\" " +
                    "attributeName=\"fill\" from=\"rgb(0,0,255)\" to=\"rgb(255,0,0)\"/>\n" +
                    "</rect>\n" +
                    "<ellipse id=\"e\" x=\"0\" y=\"0\" width=\"0\" height=\"0\"" +
                    " fill=\"rgb(0,0,0)\"" +
                    " visibility=\"hidden\">\n" +
                    "<set attributeName=\"visibility\"" +
                    " to=\"visible\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"cx\" to=\"0\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"cy\" to=\"0\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"ry\" to=\"5\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"rx\" to=\"5\" begin=\"0ms\" dur=\"0.0ms\" " +
                    "fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" end=\"10000ms\"" +
                    " attributeName=\"cx\" from=\"0\" to=\"3\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" end=\"10000ms\"" +
                    " attributeName=\"cy\" from=\"0\" to=\"2\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"cx\" from=\"3\" to=\"0\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"cy\" from=\"2\" to=\"0\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"ry\" from=\"5\" to=\"2\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"rx\" from=\"5\" to=\"1\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\"" +
                    " attributeName=\"fill\" from=\"rgb(255,0,0)\" to=\"rgb(0,0,255)\"/>\n" +
                    "</ellipse>\n" +
                    "<ellipse id=\"Dave\" x=\"0\" y=\"0\" width=\"0\" height=\"0\" " +
                    "fill=\"rgb(0,0,0)\" visibility=\"hidden\"/>\n" +
                    "</ellipse>\n" +
                    "</svg>\n");
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
            "canvas 0 0 500 500\n" +
                    "\n" +
                    "<svg width=\"500\" height=\"500\" version=\"1.1\"" +
                    " xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "</svg>\n");
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
                    "shape Dave ellipse\n" +
                    "\n" +
                    "<svg width=\"500\" height=\"500\" version=\"1.1\"" +
                    " xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "<rect id=\"r\" x=\"0\" y=\"0\" width=\"0\" height=\"0\" " +
                    "fill=\"rgb(0,0,0)\"" +
                    " visibility=\"hidden\">\n" +
                    "<set attributeName=\"visibility\" to=\"visible\" " +
                    "begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"x\" to=\"0\" begin=\"0ms\"" +
                    " dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"y\" to=\"0\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"height\" to=\"5\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"width\" to=\"5\" begin=\"0ms\" dur=\"0.0ms\" " +
                    "fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" end=\"10000ms\" " +
                    "attributeName=\"fill\" from=\"rgb(255,0,0)\" to=\"rgb(0,0,255)\" " +
                    "fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"height\" from=\"5\" to=\"2\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"width\" from=\"5\" to=\"1\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"20000ms\" end=\"40000ms\" " +
                    "attributeName=\"x\" from=\"0\" to=\"3\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"20000ms\" end=\"40000ms\" " +
                    "attributeName=\"y\" from=\"0\" to=\"2\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"20000ms\" end=\"40000ms\" " +
                    "attributeName=\"height\" from=\"2\" to=\"5\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"20000ms\" end=\"40000ms\" " +
                    "attributeName=\"width\" from=\"1\" to=\"5\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"20000ms\" end=\"40000ms\" " +
                    "attributeName=\"fill\" from=\"rgb(0,0,255)\" to=\"rgb(255,0,0)\"/>\n" +
                    "</rect>\n" +
                    "<ellipse id=\"e\" x=\"0\" y=\"0\" width=\"0\" height=\"0\" " +
                    "fill=\"rgb(0,0,0)\"" +
                    " visibility=\"hidden\">\n" +
                    "<set attributeName=\"visibility\" to=\"visible\" begin=\"0ms\"" +
                    " dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"cx\" to=\"0\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"cy\" to=\"0\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"ry\" to=\"5\" begin=\"0ms\" dur=\"0.0ms\"/>\n" +
                    "<set attributeName=\"rx\" to=\"5\" begin=\"0ms\" dur=\"0.0ms\"" +
                    " fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" end=\"10000ms\" " +
                    "attributeName=\"cx\" from=\"0\" to=\"3\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"0ms\" end=\"10000ms\" " +
                    "attributeName=\"cy\" from=\"0\" to=\"2\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"cx\" from=\"3\" to=\"0\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\"" +
                    " attributeName=\"cy\" from=\"2\" to=\"0\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\"" +
                    " attributeName=\"ry\" from=\"5\" to=\"2\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"rx\" from=\"5\" to=\"1\" fill=\"freeze\"/>\n" +
                    "<animate attributeType=\"xml\" begin=\"10000ms\" end=\"20000ms\" " +
                    "attributeName=\"fill\" from=\"rgb(255,0,0)\" to=\"rgb(0,0,255)\"/>\n" +
                    "</ellipse>\n" +
                    "<ellipse id=\"Dave\" x=\"0\" y=\"0\" width=\"0\" height=\"0\"" +
                    " fill=\"rgb(0,0,0)\" visibility=\"hidden\"/>\n" +
                    "</ellipse>\n" +
                    "</svg>\n");
    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
    PrintStream newStream2 = new PrintStream(stream);
    System.setOut(newStream2);
    testView.render();
    System.out.flush();
    System.setOut(original);
    assertEquals(stream2.toString(),
            "canvas 0 0 500 500\n" +
                    "\n" +
                    "<svg width=\"500\" height=\"500\" version=\"1.1\"" +
                    " xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "</svg>\n");
  }
}