package cs3500.animator.view.controls;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import cs3500.animator.model.Parameters;
import cs3500.animator.view.EditorFeatures;

/**
 * Represents a JFrame that creates a dialog for creating keyframes.
 */
public class KeyframeCreateFrame extends JFrame {

  /**
   * Creates a new KeyframeCreateFrame and forwards commands to the given controller.
   */
  KeyframeCreateFrame(EditorFeatures controller) {
    super();
    JTextField time = new JTextField();
    JTextField x = new JTextField();
    JTextField y = new JTextField();
    JTextField height = new JTextField();
    JTextField width = new JTextField();
    JTextField colorR = new JTextField();
    JTextField colorG = new JTextField();
    JTextField colorB = new JTextField();

    JComponent[] inputs = new JComponent[]{new JLabel("Time"), time, new JLabel("X"),
        x, new JLabel("Y"), y, new JLabel("Height"), height, new JLabel("Width"),
        width, new JLabel("Color Red"), colorR, new JLabel("Color Green"), colorG,
        new JLabel("Color Blue"), colorB};

    int result = JOptionPane.showConfirmDialog(this, inputs,
        "Create a Keyframe", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
      controller.insertKeyFrame(new Parameters(Integer.parseInt(time.getText()),
          Integer.parseInt(height.getText()), Integer.parseInt(width.getText()),
          new Point.Double(Double.parseDouble(x.getText()), Double.parseDouble(y.getText())),
          new Color(Integer.parseInt(colorR.getText()), Integer.parseInt(colorG.getText()),
              Integer.parseInt(colorB.getText()))));
    }
  }
}
