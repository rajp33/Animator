package cs3500.animator.view.controls;

import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Represents a List Selection Listener for selecting shapes.
 */
public class KeyframeTableSelectionListener implements ListSelectionListener {
  private JButton button;

  /**
   * Creates a new List Selection Listener that listens for updates in the shape table.
   * @param button button to enable
   */
  public KeyframeTableSelectionListener(JButton button) {
    super();
    this.button = button;
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      ListSelectionModel model = (ListSelectionModel) e.getSource();
      button.setEnabled(!model.isSelectionEmpty());
    }
  }
}
