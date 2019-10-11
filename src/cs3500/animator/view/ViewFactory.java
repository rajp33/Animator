package cs3500.animator.view;

import java.util.Arrays;

import cs3500.animator.model.Animation;

/**
 * A factory for creating specific implementations of views.
 */
public enum ViewFactory {
  Text("text", TextualView::new),
  SVG("svg", SVGAnimationView::new),
  Visual("visual", VisualView::new);

  private final String type;
  private final ViewFactoryFunction function;

  ViewFactory(String type, ViewFactoryFunction factoryFunction) {
    this.type = type;
    this.function = factoryFunction;
  }

  /**
   * Creates a concrete Implementation of AnimationView given the viewType.
   *
   * @param viewType   string key-word representing view implementation
   * @param animation  args to pass into constructor
   * @param outputName name of file/view to output.
   * @return an instance of the implementation
   * @throws IllegalArgumentException when the view type cannot be found
   */
  public static AnimationView create(String viewType, Animation animation, String outputName) {
    for (ViewFactory view : ViewFactory.values()) {
      if (view.type.equalsIgnoreCase(viewType)) {
        return view.function.create(animation, outputName);
      }
    }
    throw new IllegalArgumentException("View type must be one of the following: "
            + Arrays.deepToString(ViewFactory.values()));
  }

  /**
   * A functional interface that instantiates an AnimationView given an animation and an
   * outputName.
   */
  @FunctionalInterface
  private interface ViewFactoryFunction {
    AnimationView create(Animation animation, String outputName);
  }
}