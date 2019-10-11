import org.junit.Test;

import java.awt.event.ActionEvent;
import java.io.StringReader;

import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicButtonListener;


import cs3500.animator.controller.Controller;
import cs3500.animator.view.AnimationEditorViewImpl;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the animation editor view with action Listeners.
 */
public class TestAnimationEditorView {
  private Controller controller;
  private AnimationEditorViewImpl animeditor;
  private ActionEvent play;
  private ActionEvent pause;
  private ActionEvent restart;
  private ActionEvent loopingEnabled;
  private ActionEvent loopingDisabled;



  private void init() {
    Controller controller2;
    ChangeEvent timeChange;
    ChangeEvent speedChange;
    JSlider speedSlider;
    Readable readable;
    JSlider timeSlider;
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
    speedSlider = new JSlider();
    timeSlider = new JSlider();
    speedSlider.addChangeListener(new BasicButtonListener(new JButton()));
    timeSlider.addChangeListener(new BasicButtonListener(new JButton()));
    controller = new Controller(readable, "edit", "test input",
            "test output", 10);
    controller2 = new Controller(readable, "edit", "test input",
            "test output", 10);
    animeditor = new AnimationEditorViewImpl(5, 20,
            20, 100);
    animeditor.addSubscriber(controller);
    play = new ActionEvent("", 1234, "play");
    pause = new ActionEvent("", 1234, "pause");
    restart = new ActionEvent("", 1234, "restart");
    loopingDisabled = new ActionEvent("", 1234, "loopingDisabled");
    loopingEnabled = new ActionEvent("", 1234, "loopingEnabled");
    timeChange = new ChangeEvent(timeSlider);
    speedChange = new ChangeEvent(speedSlider);
  }


  //Unable to do tests over time passing because the Timer is in the controller. No Access.
  @Test
  public void testActionPerformed() {
    init();
    controller.runController();
    animeditor.actionPerformed(play);
    animeditor.actionPerformed(pause);
    animeditor.actionPerformed(restart);
    assertEquals(controller.getData().keySet().toString(),
            "[Rectangle: R . 100.0x50.0,  at Point2D.Double[178.0, 178.0]. " +
                    "Color: java.awt.Color[r=255,g=0,b=0], Ellipse: C. 60.0x120.0 at " +
                    "(Point2D.Double[440.0, -44.0]. Color: java.awt.Color[r=0,g=0,b=255]. ]");
    animeditor.actionPerformed(loopingDisabled);
    controller.setFrame(100);
    controller.actionPerformed(null);
    assertEquals(controller.getData().keySet().toString(),
            "[Rectangle: R . 0.0x0.0,  at Point2D.Double[0.0, 0.0]. " +
                    "Color: java.awt.Color[r=255,g=255,b=255]," +
                    " Ellipse: C. 0.0x0.0 at (Point2D.Double[0.0, 0.0]." +
                    " Color: java.awt.Color[r=255,g=255,b=255]. ]");
    animeditor.actionPerformed(loopingEnabled);
    controller.actionPerformed(null);
    assertEquals(controller.getData().keySet().toString(),
            "[Rectangle: R . 100.0x50.0,  at Point2D.Double[178.0, 178.0]. Color: " +
                    "java.awt.Color[r=255,g=0,b=0], Ellipse: C. 60.0x120.0 at " +
                    "(Point2D.Double[440.0, -44.0]. Color: java.awt.Color[r=0,g=0,b=255]. ]");
  }

  @Test(expected = IllegalStateException.class)
  public void attemptRender() {
    init();
    animeditor.render();
  }
}
