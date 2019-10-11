package cs3500.animator.view.controls;

import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import cs3500.animator.model.ShapeFactory;
import cs3500.animator.view.EditorFeatures;

/**
 * Represents a JFrame that contains a dialog to create a shape.
 */
class ShapeCreateFrame extends JFrame {

  /**
   * Create a new ShapeCreateFrame.
   * @param controller controller to listen to results
   */
  ShapeCreateFrame(EditorFeatures controller) {
    super();
    JTextField name = new JTextField();
    String[] shapeTypes = new String[ShapeFactory.values().length];

    for (int i = 0; i < ShapeFactory.values().length; i++) {
      shapeTypes[i] = ShapeFactory.values()[i].toString();
    }

    JComboBox shapeType = new JComboBox(shapeTypes);
    JComponent[] input = new JComponent[] { new JLabel("Shape Name"), name,
        new JLabel("Shape Type"), shapeType};

    int result = JOptionPane.showConfirmDialog(this, input, "Create a Shape",
        JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
      controller.addShape(name.getText(), shapeType.getSelectedItem().toString());
    }
  }

}
