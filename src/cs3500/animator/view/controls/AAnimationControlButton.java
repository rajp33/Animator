package cs3500.animator.view.controls;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

abstract class AAnimationControlButton extends JButton {

  /**
   * Represents a button used to control an animation.
   *
   * @param text represents the text on the button.
   * @param a    the actionListener.
   */
  AAnimationControlButton(String text, ActionListener a) {
    super(text);
    setActionCommand(text);
    addActionListener(a);
  }

  @Override
  protected void paintComponent(Graphics g) {
    g.setColor(Color.YELLOW);

    if (this.getModel().isArmed()) {
      g.drawImage(this.getScalableIcon((getText() + "-armed.png").toLowerCase()),
              0, 0, this);
    } else if (this.getModel().isPressed()) {
      g.drawImage(this.getScalableIcon((getText() + "-pressed.png").toLowerCase()),
              0, 0, this);
    } else {
      g.drawImage(this.getScalableIcon((getText() + "-disabled.png").toLowerCase()),
              0, 0, this);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    size.width = size.height = Math.min(size.width, size.height);
    return size;
  }

  private Image getScalableIcon(String filename) {
    Image image = new ImageIcon(filename).getImage().getScaledInstance(-1, this.getHeight(),
            Image.SCALE_SMOOTH);
    image = new ImageIcon(image).getImage();
    return image;
  }
}
