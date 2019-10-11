package cs3500.animator.view.controls;

import java.awt.event.ActionListener;

/**
 * Represents a play button in the Animation Editor. A PlayButton is a custom JButton in the shape
 * of a play triangle.
 */
public class PlayButton extends AAnimationControlButton {

  /**
   * Constructs a new PlayButton and sets its Action command to play.
   */
  public PlayButton(ActionListener a) {
    super("play", a);
  }
}