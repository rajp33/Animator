import org.junit.Test;

import javax.swing.JButton;
import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs3500.animator.view.controls.KeyframeTableSelectionListener;

import static junit.framework.TestCase.assertTrue;

/**
 * Tests the ListSelectionListener for the Tables.
 */
public class TestKeyframeTableSelectionListener {
  private JButton removeButton;
  private ListSelectionListener listener;
  private ListSelectionEvent eventOne;

  //initializes the data for testing.
  private void init() {
    removeButton = new JButton("Remove");
    removeButton.setActionCommand("remove");
    listener = new KeyframeTableSelectionListener(removeButton);
    eventOne = new ListSelectionEvent(new DefaultListSelectionModel(),
            0, 1, false);
  }

  @Test
  public void testValueChanged() {
    init();
    assertTrue(removeButton.isEnabled());
    listener.valueChanged(eventOne);
    assertTrue(!removeButton.isEnabled());
    listener.valueChanged(eventOne);
    assertTrue(!removeButton.isEnabled());
  }
}
