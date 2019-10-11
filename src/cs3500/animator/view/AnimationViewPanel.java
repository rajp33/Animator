package cs3500.animator.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.swing.JPanel;

import cs3500.animator.model.Keyframe;
import cs3500.animator.model.ReadOnlyShape;
import cs3500.animator.model.ShapeFactory;
import cs3500.animator.view.controls.AnimationDataDisplay;

/**
 * Represents a view for the {@link VisualView} view-model.
 */
public class AnimationViewPanel extends JPanel implements AnimationDataDisplay {
  private List<ReadOnlyShape> shapes;
  private final int height;
  private final int width;

  /**
   * Create an AnimationViewPanel given the shapes and the width and height of the canvas.
   *
   * @param shapes list of shapes to display
   * @param w      width of canvas
   * @param h      height of canvas
   */
  public AnimationViewPanel(List<ReadOnlyShape> shapes, int w, int h) {
    this.shapes = shapes;
    this.width = w;
    this.height = h;
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (ReadOnlyShape shape : shapes) {
      ShapeFactory.draw(shape, g);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(width, height);
  }

  @Override
  public void setData(Map<ReadOnlyShape, SortedSet<Keyframe>> data) {
    this.shapes = new ArrayList<>(data.keySet());
  }

  @Override
  public void addController(EditorFeatures controller) {
    throw new UnsupportedOperationException("Adding a controller to this panel is unnecessary");
  }

  @Override
  public void setSelected(ReadOnlyShape shape) {
    throw new UnsupportedOperationException("The animation canvas does not need to display that"
            + " a shape is selected");
  }
}