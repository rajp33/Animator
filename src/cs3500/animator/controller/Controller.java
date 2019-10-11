package cs3500.animator.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.swing.Timer;

import cs3500.animator.model.Animation;
import cs3500.animator.model.AnimationEditor;
import cs3500.animator.model.Keyframe;
import cs3500.animator.model.KeyframeAnimation;
import cs3500.animator.model.ReadOnlyKeyframe;
import cs3500.animator.model.ReadOnlyShape;
import cs3500.animator.model.threadableAnimation.ThreadableAnimation;
import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.AnimationEditorView;
import cs3500.animator.view.AnimationEditorViewImpl;
import cs3500.animator.view.AnimationView;
import cs3500.animator.view.EditorFeatures;
import cs3500.animator.view.ViewFactory;
import cs3500.animator.model.ShapeFactory;

/**
 * Represents an implementation of an {@link AnimationController}. Controls and handles key/mouse
 * events and controls the animator when in edit mode.
 */
public class Controller implements AnimationController, EditorFeatures, ActionListener {
  protected Animation model;
  private final AnimationView view;
  private AnimationEditorView editorView;
  private final AnimationEditor editorModel;
  int canvasX;
  int canvasY;
  private int canvasWidth;
  private int canvasHeight;
  private int endtime;
  private boolean playing;
  private boolean looping;
  private Timer timer = new Timer(100, this);
  private int frame;
  private int speed;
  private String selected;
  private double framePerSecond;


  /**
   * Represents the controller for an animation. Contains a model and a view.
   *
   * @param input      the readable input to get data for the initial animation.
   * @param viewType   how the data is going to be represented.
   * @param inputName  the name of the input.
   * @param outputName the name of the output file.
   * @param speed      the speed of the animation.
   */
  public Controller(Readable input, String viewType, String inputName,
                    String outputName, int speed) {
    this.frame = 0;
    this.speed = speed;
    this.playing = false;
    this.looping = false;
    AnimationBuilder<AnimationEditor> builder = new KeyframeAnimation.Builder();

    //Threading is too expensive in overhead, not worth using.
    //AnimationBuilder<AnimationEditor> builder = new ThreadableAnimation.TABuilder();
    this.model = editorModel = AnimationReader.parseFile(input, builder);
    List<Integer> canvasParams = model.getCanvasParams();
    this.canvasX = canvasParams.get(0);
    this.canvasY = canvasParams.get(1);
    this.canvasWidth = canvasParams.get(2);
    this.canvasHeight = canvasParams.get(3);
    this.endtime = model.getEndTime();
    List<Integer> bounds = model.getCanvasParams();

    if (viewType.equals("edit")) {
      this.editorView = new AnimationEditorViewImpl(speed, canvasWidth, canvasHeight,
              this.endtime);
      editorView.setData(editorModel.getData());
      editorView.addSubscriber(this);
      view = editorView;
      timer = new Timer(1000 / speed, this);
    } else {
      model.setSpeed(speed);
      view = ViewFactory.create(viewType, model, outputName);
    }
  }

  @Override
  public void runController() {
    view.render();
  }

  @Override
  public void setPlaying() {
    if (!playing) {
      timer.start();
      playing = true;
    }
  }

  @Override
  public void setPaused() {
    timer.stop();
    playing = false;
  }

  @Override
  public void restart() {
    timer.stop();
    playing = false;
    this.frame = 0;
    editorModel.setTime(frame);
    this.actionPerformed(null);
  }

  @Override
  public void setLooping(boolean enabled) {
    looping = enabled;
  }

  @Override
  public void insertKeyFrame(ReadOnlyKeyframe parameters) {
    editorModel.insertKeyFrame(selected, parameters);
    editorView.setData(editorModel.getData());
    editorView.setEndTime(editorModel.getEndTime());
    this.actionPerformed(new ActionEvent(this, 0, "timeMoved"));
  }

  @Override
  public void editKeyframe(int time, String name, Object newValue) {
    Map<ReadOnlyShape, SortedSet<Keyframe>> data = editorModel.getData();
    for (ReadOnlyShape shape : data.keySet()) {
      if (shape.getName().equals(selected)) {
        List<Keyframe> keyframes = new ArrayList<>(data.get(shape));
        for (Keyframe keyFrame : keyframes) {
          if (keyFrame.getTime() == time) {
            switch (name.toLowerCase()) {
              case "time":
                keyFrame.setTime(Integer.parseInt((String) newValue));
                break;
              case "x":
                keyFrame.setPosition((Double.parseDouble((String) newValue)),
                    keyFrame.getPosition().y);
                break;
              case "y":
                keyFrame.setPosition(keyFrame.getPosition().x,
                    Double.parseDouble((String) newValue));
                break;
              case "height":
                keyFrame.setDimensions(Double.parseDouble((String) newValue), keyFrame.getWidth());
                break;
              case "width":
                keyFrame.setDimensions(keyFrame.getHeight(), Double.parseDouble((String) newValue));
                break;
              case "color":
                keyFrame.setColor((Color) newValue);
                break;
              default:
                throw new IllegalStateException("Row doesn't exist.");
            }
          }
        }
      }
    }
    editorView.setData(editorModel.getData());
    this.actionPerformed(new ActionEvent(this, 0, "timeMoved"));
  }

  @Override
  public void removeKeyFrame(int time) {
    editorModel.removeKeyFrame(selected, time);
    editorView.setData(editorModel.getData());
    editorView.setEndTime(editorModel.getEndTime());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (frame + 1 >= endtime && looping) {
      frame = 0;
    } else if (frame + 1 >= endtime) {
      frame = endtime;
      this.setPaused();
    } else {
      frame++;
      editorModel.setTime(frame);
      view.refresh(frame);
      framePerSecond++;
    }
  }

  @Override
  public void setFrame(int frame) {
    this.frame = frame - 1;
    this.actionPerformed(new ActionEvent(this, 0, "timeMoved"));
  }

  @Override
  public void setSpeed(int speed) {
    this.speed = speed;
    this.timer.setDelay(1000 / speed);
    editorView.setSpeed(speed);
  }

  @Override
  public void removeShape(String name) {
    editorModel.removeShape(name);
    editorView.setData(editorModel.getData());
    editorView.setEndTime(model.getEndTime());
    this.actionPerformed(new ActionEvent(this, 0, "timeMoved"));
  }

  @Override
  public void setSelectedShape(String name) {
    if (name != null) {
      this.selected = name;
      for (ReadOnlyShape shape : editorModel.getData().keySet()) {
        if (shape.getName().equals(name)) {
          this.editorView.setSelected(shape);
        }
      }
    }
  }

  @Override
  public void addShape(String name, String type) {
    editorModel.addShape(ShapeFactory.createShape(name, type));
    editorView.setData(editorModel.getData());
  }

  @Override
  public double getFPS() {
    double fps = framePerSecond;
    framePerSecond = 0;
    return fps;
  }

  @Override
  public Map<ReadOnlyShape, SortedSet<Keyframe>> getData() {
    return editorModel.getData();
  }
}
