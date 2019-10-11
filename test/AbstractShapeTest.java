import org.junit.Test;

import java.awt.Point;
import java.awt.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cs3500.animator.model.Ellipse;
import cs3500.animator.model.Parameters;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.Shape;

/**
 * Test for implementations of AShape.
 */
public abstract class AbstractShapeTest {

  /**
   * Constructs an instance of the Shape class given the parameters.
   *
   * @param h    height
   * @param w    width
   * @param x    x-position
   * @param y    y-position
   * @param r    red value
   * @param g    green value
   * @param b    blue value
   * @param name name
   * @return a specific implementation of shape
   */
  protected abstract Shape params(int h, int w, int x, int y, int r, int g, int b, String name);

  /**
   * Helper class to test Rectangles while testing AbstractShapes.
   */
  public static final class RectangleTest extends AbstractShapeTest {

    @Override
    protected Shape params(int h, int w, int x, int y, int r, int g, int b, String name) {
      return new Rectangle(h, w, x, y, r, g, b, name);
    }
  }

  /**
   * Helper class to test Ellipses while testing AbstractShapes.
   */
  public static final class EllipseTest extends AbstractShapeTest {
    @Override
    protected Shape params(int h, int w, int x, int y, int r, int g, int b, String name) {
      return new Ellipse(h, w, x, y, r, g, b, name + "yuh");
    }
  }

  @Test
  public void testUpdateRecieved() {
    Shape shape = params(100, 100, 100, 100, 255, 255, 255, "Dave");
    shape.updateRecieved(new Parameters(1, 10, 10, new Point.Double(10, 10)
            , Color.BLACK));

    assertEquals(10, (int) shape.getHeight());
    assertEquals(10, (int) shape.getWidth());
    assertEquals(new Point.Double(10, 10), shape.getPosition());
    assertEquals(Color.BLACK, shape.getColor());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testisNameUnique() {
    new Ellipse(30, 30, 30, 30, 30, 30, 30, "Ditto");
    new Rectangle(500, 500, 500, 500, 0, 0, 0, "Ditto");
  }

  @Test
  public void getHeight() {
    Shape shape = params(1, 1, 1, 1, 1, 1, 1, "Scyther");
    assertEquals(1, (int) shape.getHeight());
  }

  @Test
  public void getWidth() {
    Shape shape = params(1, 1, 1, 1, 1, 1, 1, "Blastoise");
    assertEquals(1, (int) shape.getWidth());
  }

  @Test
  public void getColor() {
    Shape shape = params(1, 1, 1, 1, 1, 1, 1, "Shpeal");
    assertEquals(new Color(1, 1, 1), shape.getColor());
  }

  @Test
  public void getPosition() {
    Shape shape = params(1, 1, 1, 1, 1, 1, 1, "Blaziken");
    assertEquals(new Point.Double(1, 1), shape.getPosition());
  }

  @Test
  public void setHeight() {
    Shape shape = params(1, 1, 1, 1, 1, 1, 1, "Beedrill");
    shape.setHeight(300);
    assertEquals(300, (int) shape.getHeight());
  }

  @Test
  public void setWidth() {
    Shape shape = params(1, 1, 1, 1, 1, 1, 1, "Shinx");
    shape.setWidth(300);
    assertEquals(300, (int) shape.getWidth());
  }

  @Test
  public void setColor() {
    Shape shape = params(1, 1, 1, 1, 1, 1, 1, "Onix");
    shape.setColor(Color.WHITE);
    assertEquals(Color.WHITE, shape.getColor());
  }

  @Test
  public void setPosition() {
    Shape shape = params(1, 1, 1, 1, 1, 1, 1, "Rhydon");
    shape.setPosition(new Point.Double(10, 10));
    assertEquals(new Point.Double(10, 10), shape.getPosition());
  }

  @Test
  public void getName() {
    Shape shape = params(1, 1, 1, 1, 1, 1, 1, "Ponyta");
    assertTrue(shape.getName().contains("Ponyta"));
  }
}
