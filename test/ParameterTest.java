import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.awt.Color;

import cs3500.animator.model.Parameters;

/**
 * Tests for the Parameter class.
 */
public class ParameterTest {

  @Test
  public void testToString() {
    Parameters test = new Parameters(1, 1, 1,
            new Point.Double(10, 10), Color.BLACK);

    assertEquals(test.toString(), "1 10 10 1 1 0 0 0");
  }
}
