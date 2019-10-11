package cs3500.animator.view.controls;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Represents the Panel that contains all of the contorld for editing an animation.
 */
public class EditorPanel extends JPanel {

  /**
   * Represents a panel for editing an animation.
   * @param shapeTable represents the table of shapes.
   * @param keyframeTable represents a table of keyframes.
   */
  public EditorPanel(JPanel shapeTable, JPanel keyframeTable) {
    this.setPreferredSize(new Dimension(300, this.getPreferredSize().height));
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(shapeTable);
    this.add(Box.createRigidArea(new Dimension(1, 10)));
    this.add(keyframeTable);
  }
}
