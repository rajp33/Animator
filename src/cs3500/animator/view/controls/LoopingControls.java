package cs3500.animator.view.controls;

import java.awt.event.ActionListener;


import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import cs3500.animator.view.AnimationEditorViewImpl;

/**
 * Represents the controls that determine if an animation loops.
 */
public class LoopingControls extends JPanel {

  /**
   * Constructs a Looping controls panel.
   * @param a the action listener.
   */
  public LoopingControls(ActionListener a) {
    super();
    JRadioButton enabled;
    JRadioButton disabled;
    JLabel loopingLabel;

    this.setBackground(AnimationEditorViewImpl.BG_COLOR);
    ButtonGroup bGroup = new ButtonGroup();
    enabled = new JRadioButton("enabled");
    disabled = new JRadioButton("disabled");
    loopingLabel = new JLabel("Looping:");

    disabled.setSelected(true);

    bGroup.add(enabled);
    bGroup.add(disabled);

    this.add(loopingLabel);
    this.add(enabled);
    this.add(disabled);

    enabled.setActionCommand("loopingEnabled");
    disabled.setActionCommand("loopingDisabled");
    enabled.addActionListener(a);
    disabled.addActionListener(a);
  }

//  /**
//   * Sets the radio buttons to be opposite of each other.
//   * @param enable
//   */
//  void isEnabled(boolean enable) {
//    this.enabled.setSelected(enable);
//    this.disabled.setSelected(!enable);
//  }
}
