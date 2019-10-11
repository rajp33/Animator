package cs3500.animator.view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import cs3500.animator.model.Animation;
import cs3500.animator.model.ReadOnlyShape;

/**
 * Represents a visual view for an animation. Uses {@link Graphics}.
 */
public class VisualView extends AView implements ActionListener {
  private final JFrame window;
  private Timer timer;
  private int frame;

  /**
   * Constructs a visual view of the Animation given the model and name of the window to output to.
   *
   * @param animation  animation to display
   * @param outputName name of the window that will display animation
   */
  VisualView(Animation animation, String outputName) {
    super(animation, outputName);
    this.frame = 0;
    this.timer = new Timer(1000 / speed, this);

    if (outputName == null) {
      outputName = "animator";
    }
    List<ReadOnlyShape> shapes = animation.getShapes();

    //create window
    this.window = new JFrame(outputName);
    this.window.setLayout(new BorderLayout());
    this.window.setSize(width, height);
    this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //create canvas
    JPanel canvas = new AnimationViewPanel(shapes, width, height);
    canvas.setLocation(x, y);
    JScrollPane scroll = new JScrollPane(canvas);

    //add canvas to window
    this.window.add(scroll, BorderLayout.CENTER);
  }

  @Override
  public void render() {
    this.window.setVisible(true);
    timer.start();
  }

  @Override
  public void refresh(int time) {
    this.animation.setTime(time);
    this.window.repaint();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (frame >= animation.getEndTime()) {
      timer.stop();
      window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
      window.dispose();
    } else {
      frame++;
      this.refresh(frame);
    }
  }
}