package cs3500.animator.view.controls;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

/**
 * Represents a slider object to use in an AnimationEditor.
 */
public class ASlider extends JSlider {

  /**
   * The constructor for a generic slier in an animation.
   *
   * @param min            minimum value of the slider.
   * @param max            maximum value of the slider.
   * @param initValue      initial value of the slider.
   * @param startLabel     label on the start of the slider.
   * @param endLabel       label on the end of the slider.
   * @param changeListener the changeListener for the slider.
   * @param name           the name of the slider.
   */
  public ASlider(int min, int max, int initValue, String startLabel, String endLabel,
                 ChangeListener changeListener, String name) {
    super(JSlider.HORIZONTAL, min, max, initValue);
    setName(name);
    setPaintTicks(false);
    Dictionary<Integer, JComponent> labels = new Hashtable<>();
    labels.put(0, new JLabel(startLabel));
    labels.put(this.getMaximum(), new JLabel(endLabel));
    setLabelTable(labels);
    setPaintLabels(true);
    addChangeListener(changeListener);
  }
}
