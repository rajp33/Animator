import org.junit.Test;


import java.awt.Color;
import java.awt.Point;
import java.io.StringReader;
import java.util.Map;
import java.util.SortedSet;

import cs3500.animator.controller.Controller;
import cs3500.animator.model.Keyframe;
import cs3500.animator.model.Parameters;
import cs3500.animator.model.ReadOnlyShape;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the controller for the animation editor.
 */
public class TestAnimationController {
  private Readable readable;
  private Controller controller;

  /**
   * Initializes the data before the test.
   */
  private void init() {
    readable = new StringReader("canvas 200 70 360 360\n" +
            "shape R rectangle\n" +
            "motion R 1 200 200 50 100 255 0 0 10 200 200 50 100 255 0 0\n" +
            "motion R 10 200 200 50 100 255 0 0 50 300 300 50 100 255 0 0\n" +
            "motion R 50 300 300 50 100 255 0 0 51 300 300 50 100 255 0 0\n" +
            "motion R 51 300 300 50 100 255 0 0 70 300 300 25 100 255 0 0\n" +
            "motion R 70 300 300 25 100 255 0 0 100 200 200 25 100 255 0 0\n" +
            "shape C ellipse\n" +
            "motion C 6 440 70 120 60 0 0 255 20 440 70 120 60 0 0 255\n" +
            "motion C 20 440 70 120 60 0 0 255 50 440 250 120 60 0 0 255\n" +
            "motion C 50 440 250 120 60 0 0 255 70 440 370 120 60 0 170 85\n" +
            "motion C 70 440 370 120 60 0 170 85 80 440 370 120 60 0 255 0\n" +
            "motion C 80 440 370 120 60 0 255 0 100 440 370 120 60 0 255 0");
    controller = new Controller(readable, "edit",
            "test input", "test output", 10);
  }

  /**
   * Resets the data to prevent exceptions.
   */
  private void resetState() {
    readable = null;
    controller = null;
  }

  @Test
  public void testGetData() {
    init();
    Map<ReadOnlyShape, SortedSet<Keyframe>> data = controller.getData();
    assertEquals(data.toString(),
            "{Rectangle: R . 0.0x0.0,  at Point2D.Double[0.0, 0.0]. " +
                    "Color: java.awt.Color[r=0,g=0,b=0]=[10 200 200 50 100 255 0 0, " +
                    "50 300 300 50 100 255 0 0, 51 300 300 50 100 255 0 0," +
                    " 70 300 300 25 100 255 0 0, 100 200 200 25 100 255 0 0], " +
                    "Ellipse: C. 0.0x0.0 at (Point2D.Double[0.0, 0.0]." +
                    " Color: java.awt.Color[r=0,g=0,b=0]. =[20 440 70 120 60 0 255 0," +
                    " 50 440 250 120 60 0 255 0, 70 440 370 120 60 0 85 170, " +
                    "80 440 370 120 60 0 0 255, 100 440 370 120 60 0 0 255]}");
    resetState();
  }

  //Unable to test because the action performed method repaints the JFrame.
  //A Null Pointer Exception will be thrown unless the game is run.
  @Test
  public void testSetFrame() {
    init();
    controller.runController();
    controller.setFrame(0);
    controller.actionPerformed(null);
    assertEquals(controller.getData().keySet().toString(),
            "[Rectangle: R . 100.0x50.0, " +
                    " at Point2D.Double[175.0, 175.0]. Color: java.awt.Color[r=255,g=0,b=0]," +
                    " Ellipse: C. 60.0x120.0 at (Point2D.Double[440.0, -50.0]." +
                    " Color: java.awt.Color[r=0,g=0,b=255]. ]");
    controller.setFrame(10);
    controller.actionPerformed(null);
    assertEquals(controller.getData().keySet().toString(),
            "[Rectangle: R . 100.0x50.0,  at Point2D.Double[200.0, 200.0]. " +
                    "Color: java.awt.Color[r=255,g=0,b=0], Ellipse: C. 60.0x120.0 at " +
                    "(Point2D.Double[440.0, 10.0]. Color: java.awt.Color[r=0,g=0,b=255]. ]");
    controller.setFrame(10);
    controller.actionPerformed(null);
    assertEquals(controller.getData().toString(),
            "{Rectangle: R . 100.0x50.0,  at Point2D.Double[200.0, 200.0]. " +
                    "Color: java.awt.Color[r=255,g=0,b=0]=[10 200 200 50 100 255 0 0," +
                    " 50 300 300 50 100 255 0 0, 51 300 300 50 100 255 0 0," +
                    " 70 300 300 25 100 255 0 0, 100 200 200 25 100 255 0 0]," +
                    " Ellipse: C. 60.0x120.0 at (Point2D.Double[440.0, 10.0]. " +
                    "Color: java.awt.Color[r=0,g=0,b=255]. =[20 440 70 120 60 0 255 0," +
                    " 50 440 250 120 60 0 255 0, " +
                    "70 440 370 120 60 0 85 170, 80 440 370 120 60 0 0 255," +
                    " 100 440 370 120 60 0 0 255]}");
    controller.setFrame(0);
    assertEquals(controller.getData().toString(),
            "{Rectangle: R . 100.0x50.0,  at Point2D.Double[200.0, 200.0]." +
                    " Color: java.awt.Color[r=255,g=0,b=0]=[10 200 200 50 100 255 0 0," +
                    " 50 300 300 50 100 255 0 0, 51 300 300 50 100 255 0 0," +
                    " 70 300 300 25 100 255 0 0, 100 200 200 25 100 255 0 0], " +
                    "Ellipse: C. 60.0x120.0 at (Point2D.Double[440.0, 10.0]." +
                    " Color: java.awt.Color[r=0,g=0,b=255]. =[20 440 70 120 60 0 255 0," +
                    " 50 440 250 120 60 0 255 0, 70 440 370 120 60 0 85 170," +
                    " 80 440 370 120 60 0 0 255, 100 440 370 120 60 0 0 255]}");
    resetState();
  }

  //Same issue as above. Cannot test this method.
  @Test
  public void testRestart() {
    init();
    controller.runController();
    controller.actionPerformed(null);
    assertEquals(controller.getData().toString(),
            "{Rectangle: R . 100.0x50.0,  at Point2D.Double[178.0," +
                    " 178.0]. Color: java.awt.Color[r=255,g=0,b=0]=[10 200 200 50 100 255 0 0," +
                    " 50 300 300 50 100 255 0 0, 51 300 300 50 100 255 0 0," +
                    " 70 300 300 25 100 255 0 0, 100 200 200 25 100 255 0 0]," +
                    " Ellipse: C. 60.0x120.0 at (Point2D.Double[440.0, -44.0]." +
                    " Color: java.awt.Color[r=0,g=0,b=255]. =[20 440 70 120 60 0 255 0," +
                    " 50 440 250 120 60 0 255 0, 70 440 370 120 60 0 85 170," +
                    " 80 440 370 120 60 0 0 255, 100 440 370 120 60 0 0 255]}");
    controller.setFrame(10);
    controller.actionPerformed(null);
    assertEquals(controller.getData().toString(),
            "{Rectangle: R . 100.0x50.0,  at Point2D.Double[200.0, 200.0]." +
                    " Color: java.awt.Color[r=255,g=0,b=0]=[10 200 200 50 100 255 0 0," +
                    " 50 300 300 50 100 255 0 0, 51 300 300 50 100 255 0 0," +
                    " 70 300 300 25 100 255 0 0, 100 200 200 25 100 255 0 0]," +
                    " Ellipse: C. 60.0x120.0 at (Point2D.Double[440.0, 10.0]. " +
                    "Color: java.awt.Color[r=0,g=0,b=255]. =[20 440 70 120 60 0 255 0," +
                    " 50 440 250 120 60 0 255 0, 70 440 370 120 60 0 85 170," +
                    " 80 440 370 120 60 0 0 255, 100 440 370 120 60 0 0 255]}");
    controller.restart();
    controller.actionPerformed(null);
    assertEquals(controller.getData().toString(),
            "{Rectangle: R . 100.0x50.0,  at Point2D.Double[178.0, 178.0]." +
                    " Color: java.awt.Color[r=255,g=0,b=0]=[10 200 200 50 100 255 0 0," +
                    " 50 300 300 50 100 255 0 0, 51 300 300 50 100 255 0 0," +
                    " 70 300 300 25 100 255 0 0, 100 200 200 25 100 255 0 0]," +
                    " Ellipse: C. 60.0x120.0 at (Point2D.Double[440.0, -44.0]." +
                    " Color: java.awt.Color[r=0,g=0,b=255]. =[20 440 70 120 60 0 255 0," +
                    " 50 440 250 120 60 0 255 0, 70 440 370 120 60 0 85 170," +
                    " 80 440 370 120 60 0 0 255, 100 440 370 120 60 0 0 255]}");
    resetState();
  }


  @Test
  public void testInsertKeyframe() {
    init();
    controller.runController();
    controller.insertKeyFrame(new Parameters(10, 2, 3,
            new Point.Double(2, 4), Color.YELLOW));
    controller.insertKeyFrame(new Parameters(5, 10, 20,
            new Point.Double(1, 2), Color.PINK));
    controller.insertKeyFrame(new Parameters(5, 2, 1,
            new Point.Double(3.5, 6), Color.BLACK));
    controller.insertKeyFrame(new Parameters(6, 3, 15,
            new Point.Double(2, 1), Color.BLUE));
    assertEquals(controller.getData().toString(),
            "{Rectangle: R . 0.0x0.0,  at Point2D.Double[0.0, 0.0]." +
                    " Color: java.awt.Color[r=0,g=0,b=0]=[10 200 200 50 100 255 0 0," +
                    " 50 300 300 50 100 255 0 0, 51 300 300 50 100 255 0 0," +
                    " 70 300 300 25 100 255 0 0, 100 200 200 25 100 255 0 0]," +
                    " Ellipse: C. 0.0x0.0 at (Point2D.Double[0.0, 0.0]. " +
                    "Color: java.awt.Color[r=0,g=0,b=0]. =[20 440 70 120 60 0 255 0," +
                    " 50 440 250 120 60 0 255 0, 70 440 370 120 60 0 85 170, " +
                    "80 440 370 120 60 0 0 255, 100 440 370 120 60 0 0 255]}");
    resetState();
  }

  @Test
  public void testRemoveShape() {
    init();
    controller.runController();
    controller.removeShape("R");
    assertEquals(controller.getData().toString(),
            "{Ellipse: C. 0.0x0.0 at (Point2D.Double[0.0, 0.0]. " +
                    "Color: java.awt.Color[r=0,g=0,b=0]. =[20 440 70 120 60 0 255 0," +
                    " 50 440 250 120 60 0 255 0, 70 440 370 120 60 0 85 170," +
                    " 80 440 370 120 60 0 0 255, 100 440 370 120 60 0 0 255]}");
    controller.removeShape("C");
    assertEquals(controller.getData().toString(), "{}");
    resetState();
  }

  @Test
  public void testAddShape() {
    init();
    controller.runController();
    controller.addShape("ns1", "rectangle");
    controller.addShape("ns2", "ellipse");
    assertEquals(controller.getData().keySet().toString(),
            "[Rectangle: R . 0.0x0.0,  at Point2D.Double[0.0, 0.0]. " +
                    "Color: java.awt.Color[r=0,g=0,b=0], Ellipse: C. 0.0x0.0 at " +
                    "(Point2D.Double[0.0, 0.0]. Color: java.awt.Color[r=0,g=0,b=0]. ," +
                    " Rectangle: ns1 . 0.0x0.0,  at Point2D.Double[0.0, 0.0]. Color: " +
                    "java.awt.Color[r=0,g=0,b=0], Ellipse: ns2. 0.0x0.0 at " +
                    "(Point2D.Double[0.0, 0.0]. Color: java.awt.Color[r=0,g=0,b=0]. ]");
    controller.setSelectedShape("ns1");
    controller.insertKeyFrame(new Parameters(5, 10, 20,
            new Point.Double(3.5, 6), Color.RED));
    controller.setSelectedShape("ns2");
    controller.insertKeyFrame(new Parameters(20, 3, 2,
            new Point.Double(3.5, 6), Color.RED));
    assertEquals(controller.getData().toString(),
            "{Rectangle: R . 0.0x0.0,  at Point2D.Double[0.0, 0.0]." +
                    " Color: java.awt.Color[r=0,g=0,b=0]=[10 200 200 50 100 255 0 0," +
                    " 50 300 300 50 100 255 0 0, 51 300 300 50 100 255 0 0," +
                    " 70 300 300 25 100 255 0 0, 100 200 200 25 100 255 0 0]," +
                    " Ellipse: C. 0.0x0.0 at (Point2D.Double[0.0, 0.0]. " +
                    "Color: java.awt.Color[r=0,g=0,b=0]. =[20 440 70 120 60 0 255 0," +
                    " 50 440 250 120 60 0 255 0, 70 440 370 120 60 0 85 170, 80 440" +
                    " 370 120 60 0 0 255," +
                    " 100 440 370 120 60 0 0 255], Rectangle: ns1 . 0.0x0.0," +
                    "  at Point2D.Double[0.0, 0.0]. " +
                    "Color: java.awt.Color[r=0,g=0,b=0]=[5 3 6 20 10 255 0 0], " +
                    "Ellipse: ns2. 0.0x0.0 at (Point2D.Double[0.0, 0.0]." +
                    " Color: java.awt.Color[r=0,g=0,b=0]. =[20 3 6 2 3 255 0 0]}");
    resetState();
  }

  //Unable to test run, or setSpeed with the methods in the current interface. No information
  //about the speed is provided by the conroller or any of the methods.
}
