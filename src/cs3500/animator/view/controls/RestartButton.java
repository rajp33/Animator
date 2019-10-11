package cs3500.animator.view.controls;

import java.awt.event.ActionListener;


/**
 * Represents a RestartButton. A Restart Button is just a custom JButton.
 */
public class RestartButton extends AAnimationControlButton {

  /**
   * Constructs a new RestartButton and sets it actioncommand.
   */
  public RestartButton(ActionListener a) {
    super("restart", a);
  }
}
