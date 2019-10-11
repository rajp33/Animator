package cs3500.animator.view.controls;

import java.awt.event.ActionListener;

/**
 * Represents a PauseButton. A PauseButton is simply a custom JButton in the shape of a pause
 * button.
 */
public class PauseButton extends AAnimationControlButton {

  /**
   * Constructs a new PauseButton and sets its action to pause.
   */
  public PauseButton(ActionListener a) {
    super("pause", a);
    setActionCommand("pause");
  }

}
