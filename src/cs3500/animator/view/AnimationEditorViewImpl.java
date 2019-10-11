package cs3500.animator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cs3500.animator.view.controls.EditorPanel;
import cs3500.animator.view.controls.KeyframeEditor;
import cs3500.animator.model.Animation;
import cs3500.animator.model.Keyframe;
import cs3500.animator.model.ReadOnlyShape;
import cs3500.animator.view.controls.LoopingControls;
import cs3500.animator.view.controls.PauseButton;
import cs3500.animator.view.controls.PlayButton;
import cs3500.animator.view.controls.RestartButton;
import cs3500.animator.view.controls.ASlider;
import cs3500.animator.view.controls.ShapeEditor;

/**
 * Represents an implementation of the Animation Editor View.This represents an Animation Editor
 * that displays an animation and the controls for contorlling its playback as well as
 * creating/editing shapes and keyframes.
 */
public class AnimationEditorViewImpl extends JFrame implements AnimationEditorView,
    ActionListener, ChangeListener {
  private Map<ReadOnlyShape, SortedSet<Keyframe>> data;
  private int speed;
  private AnimationViewPanel screen;
  private int frame;
  private int endTime;
  private int canvasW;
  private int canvasH;

  private JLabel speedNumber;
  private JLabel timeNumber;
  private JLabel fps;
  private JSlider timeSlider;
  private JSlider speedslider;

  private ShapeEditor shapeEditor;
  private KeyframeEditor keyframeEditor;
  private List<EditorFeatures> subscribers;

  private Timer fpsTimer;

  public static final Color BG_COLOR = Color.LIGHT_GRAY;

  /**
   * Constructs an AnimationEditorViewImpl.
   *
   * @param initialSpeed initial speed (ticks/sec)
   * @param canvasWidth  width of canvas to display animation
   * @param canvasHeight height of canvas to display animation
   */
  public AnimationEditorViewImpl(int initialSpeed, int canvasWidth, int canvasHeight, int endTime) {
    super();
    this.speed = initialSpeed;
    this.subscribers = new ArrayList<>();
    this.canvasW = canvasWidth;
    this.canvasH = canvasHeight;
    this.endTime = endTime;

    this.fpsTimer = new Timer(0, this);
    this.fpsTimer.setActionCommand("tick");
    this.fpsTimer.setDelay(1000);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    this.setSize(1000, 700);

    this.frame = 0;
  }

  @Override
  public void read(Animation animation) {
    throw new UnsupportedOperationException("This type of view should not read directly from an" +
        "animation");
  }

  @Override
  public void render() {
    if (data != null) {
      this.initEditors();
    } else {
      throw new IllegalStateException("Data must be loaded into view using setData");
    }
    this.initControls();
    this.initCanvas();
    this.setVisible(true);
  }

  @Override
  public void refresh(int time) throws UnsupportedOperationException {
    this.screen.repaint();
    this.timeSlider.setValue(time);
    this.timeNumber.setText(String.valueOf(time));
  }

  private void initCanvas() {
    this.screen = new AnimationViewPanel(new ArrayList<>(this.data.keySet()), canvasW, canvasH);
    JScrollPane scrollPane = new JScrollPane(screen);
    scrollPane.setBorder(null);
    this.add(scrollPane, BorderLayout.CENTER);
  }

  private void initEditors() {
    this.keyframeEditor = new KeyframeEditor(data);
    this.shapeEditor = new ShapeEditor();

    for (EditorFeatures controller : this.subscribers) {
      shapeEditor.addController(controller);
      keyframeEditor.addController(controller);
    }

    shapeEditor.setData(data);
    JPanel editorPanel = new EditorPanel(shapeEditor, keyframeEditor);
    this.add(editorPanel, BorderLayout.EAST);
  }

  private void initControls() {
    JPanel controls = new JPanel();
    controls.setLayout(new FlowLayout());
    controls.setBackground(AnimationEditorViewImpl.BG_COLOR);

    this.timeSlider = new ASlider(0, endTime, 0, "Start",
        "End", this, "timeSlider");
    this.speedslider = new ASlider(1, 500, speed, "Slow",
        "Fast", this, "speedSlider");

    JPanel speedLabel = new JPanel();
    speedLabel.setBackground(AnimationEditorViewImpl.BG_COLOR);
    speedLabel.add(new JLabel("Speed:"));
    this.speedNumber = new JLabel(String.valueOf(speed));
    speedLabel.add(speedNumber);

    JPanel fpsLabel = new JPanel() {
      @Override
      public Dimension getPreferredSize() {
        return new Dimension(80, 20);
      }
    };
    fps = new JLabel("0");
    fpsLabel.setBackground(AnimationEditorViewImpl.BG_COLOR);
    fpsLabel.add(fps);
    this.fpsTimer.start();

    PlayButton playButton = new PlayButton(this);
    PauseButton pauseButton = new PauseButton(this);
    RestartButton restartButton = new RestartButton(this);
    LoopingControls loopingControls = new LoopingControls(this);

    JPanel timeLabel = new JPanel();
    timeLabel.setBackground(AnimationEditorViewImpl.BG_COLOR);
    timeLabel.add(new JLabel("Frame:"));
    this.timeNumber = new JLabel(String.valueOf(frame));
    timeLabel.add(timeNumber);

    controls.add(speedslider);
    controls.add(speedLabel);
    controls.add(playButton);
    controls.add(pauseButton);
    controls.add(restartButton);
    controls.add(timeSlider);
    controls.add(timeLabel);
    controls.add(loopingControls);
    controls.add(fpsLabel);

    this.add(controls, BorderLayout.SOUTH);
  }

  @Override
  public void setData(Map<ReadOnlyShape, SortedSet<Keyframe>> shapes) {
    this.data = shapes;
    if (shapeEditor != null && keyframeEditor != null) {
      this.keyframeEditor.setData(data);
      this.shapeEditor.setData(data);
      this.screen.setData(data);
    }
  }

  @Override
  public void setEndTime(int time) {
    this.endTime = time;
    this.timeSlider.setMaximum(endTime);
  }

  @Override
  public void setSpeed(int speed) {
    this.speedNumber.setText(String.valueOf(speed));
    this.speed = speed;
    this.speedslider.setValue(speed);
  }

  @Override
  public void setSelected(ReadOnlyShape shape) {
    this.keyframeEditor.setSelected(shape);
  }

  @Override
  public void addSubscriber(EditorFeatures subscriber) {
    this.subscribers.add(subscriber);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "play":
        subscribers.forEach(EditorFeatures::setPlaying);
        break;
      case "pause":
        subscribers.forEach(EditorFeatures::setPaused);
        break;
      case "restart":
        subscribers.forEach(EditorFeatures::restart);
        break;
      case "loopingEnabled":
        subscribers.forEach((EditorFeatures subscriber) -> subscriber.setLooping(true));
        break;
      case "loopingDisabled":
        subscribers.forEach((EditorFeatures subscriber) -> subscriber.setLooping(false));
        break;
      case "tick":
        if (!subscribers.isEmpty()) {
          this.setFPS(subscribers.get(0).getFPS());
        }
        break;
      default:
        throw new IllegalStateException("Button does not exist");
    }
  }

  private void setFPS(double v) {
    this.fps.setText(String.valueOf(Math.round(v)));
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    JSlider slider = (JSlider) e.getSource();
    String name = slider.getName();
    if (name.equals("timeSlider") && !slider.getValueIsAdjusting()) {
        subscribers.forEach((EditorFeatures subscriber) -> subscriber.setFrame(slider.getValue()));
    }
    else if (name.equals("speedSlider") && !slider.getValueIsAdjusting()) {
        subscribers.forEach((EditorFeatures subscriber) ->
            subscriber.setSpeed(slider.getValue()));
    }
  }
}
