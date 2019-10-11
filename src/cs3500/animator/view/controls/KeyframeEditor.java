package cs3500.animator.view.controls;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import cs3500.animator.model.Keyframe;
import cs3500.animator.model.ReadOnlyShape;
import cs3500.animator.view.EditorFeatures;


/**
 * Represents a panel for inserting/deleting/editing keyframes of a Shape in an Animation.
 */
public class KeyframeEditor extends JPanel implements AnimationDataDisplay, TableModelListener {
  int row = -1;
  private Map<ReadOnlyShape, SortedSet<Keyframe>> data;
  private JTable table;
  private JButton addButton;
  private JButton removeButton;
  private EditorFeatures controller;
  private ReadOnlyShape selected;

  /**
   * Represents the Panel that contains the controls for editing a keyframe.
   * @param data initial data to display.
   */
  public KeyframeEditor(Map<ReadOnlyShape, SortedSet<Keyframe>> data) {
    super();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.data = data;

    //create buttons
    addButton = new JButton("Add");
    removeButton = new JButton("Remove");
    removeButton.setActionCommand("remove");
    addButton.setActionCommand("add");
    removeButton.setEnabled(false);
    addButton.setEnabled(false);

    //create table model and table
    KeyframeTableModel kModel = new KeyframeTableModel(data, null);
    this.table = new KeyframeTable(data,
        new KeyframeTableSelectionListener(removeButton), kModel, this);
    table.getModel().addTableModelListener(this);

    //layout buttons
    FlowLayout flow = new FlowLayout(FlowLayout.RIGHT);
    flow.setHgap(0);
    JPanel buttonPanel = new JPanel(flow);
    buttonPanel.add(addButton);
    buttonPanel.add(removeButton);

    //layout rest
    Dimension min = new Dimension(50, 5);
    Dimension max = new Dimension(50, Short.MAX_VALUE);
    Dimension pref = new Dimension(50, 5);

    JLabel title = new JLabel("Keyframes");
    title.setFont(new Font("Calibri", Font.BOLD, 16));
    this.add(title);
    this.add(buttonPanel);
    this.add(new Box.Filler(min, pref, max));
    this.add(new JScrollPane(table));
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    if (e.getType() == TableModelEvent.UPDATE) {
      TableModel tableModel = (TableModel) e.getSource();
      Object newValue = tableModel.getValueAt(e.getFirstRow(), e.getColumn());
      Keyframe keyframe = new ArrayList<>(data.get(selected)).get(e.getFirstRow());
      controller.editKeyframe(keyframe.getTime(), tableModel.getColumnName(e.getColumn()),
          newValue);
    }
  }

  @Override
  public void setSelected(ReadOnlyShape shape) {
    this.selected = shape;
    TableModel tableModel = new KeyframeTableModel(data, selected);
    tableModel.addTableModelListener(this);
    table.setModel(tableModel);
    addButton.setEnabled(true);
    removeButton.setEnabled(true);
  }

  @Override
  public void setData(Map<ReadOnlyShape, SortedSet<Keyframe>> data) {
    this.data = data;
    KeyframeTableModel model = new KeyframeTableModel(data, selected);
    model.addTableModelListener(this);
    table.setModel(model);
  }

  @Override
  public void addController(EditorFeatures controller) {
    this.controller = controller;
    addButton.addActionListener(new KeyframeEditListener(controller));
    removeButton.addActionListener(new KeyframeEditListener(controller));
  }

  /**
   * Represents a listener for keyframe updates.
   */
  private class KeyframeEditListener implements ActionListener {
    EditorFeatures controller;

    /**
     * Construct a new KeyframeEditListener.
     */
    KeyframeEditListener(EditorFeatures controller) {
      super();
      this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      switch (e.getActionCommand()) {
        case "add":
          new KeyframeCreateFrame(controller);
          break;
        case "remove":
          controller.removeKeyFrame((int) table.getValueAt(table.getSelectedRow(), 0));
          break;
        default:
          return;
      }
    }
  }


  /**
   * Represents a JTable for displaying keyframes.
   */
  private class KeyframeTable extends JTable {
    private TableModelListener listener;

    /**
     * Construct a new KeyFrame table.
     *
     * @param data       data to populate table
     * @param l          listener that listens to selection updates
     * @param tableModel initial table model
     * @param tL         listener that listens to table model updates
     */
    KeyframeTable(Map<ReadOnlyShape, SortedSet<Keyframe>> data, ListSelectionListener l,
                  TableModel tableModel, TableModelListener tL) {
      super();
      this.setModel(tableModel);
      this.setFillsViewportHeight(true);
      this.getSelectionModel().addListSelectionListener(l);
      this.listener = tL;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return column != 0;
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
      if (column == 5) {
        return new ColorRendererCell();
      } else {
        return super.getCellRenderer(row, column);
      }
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
      if (column == 5) {
        return new ColorPicker(table.getModel(), listener, row, column);
      } else {
        return super.getCellEditor(row, column);
      }
    }
  }

  /**
   * Represents the TableModel object to provide data to the KeyframeEditorTable.
   */
  private class KeyframeTableModel extends AbstractTableModel {
    private Map<ReadOnlyShape, SortedSet<Keyframe>> data;
    private List<Keyframe> keyframes;
    private ReadOnlyShape selected;
    private int row;

    /**
     * Create a model using the given data.
     *
     * @param data shape/keyframe data
     */
    KeyframeTableModel(Map<ReadOnlyShape, SortedSet<Keyframe>> data, ReadOnlyShape selected) {
      super();
      this.data = data;
      this.selected = selected;
      if (selected == null || data.get(selected) == null) {
        this.keyframes = new ArrayList<>();
      } else {
        this.keyframes = new ArrayList<>(data.get(selected));
      }
    }

    @Override
    public String getColumnName(int column) {
      switch (column) {
        case 0:
          return "Time";
        case 1:
          return "X";
        case 2:
          return "Y";
        case 3:
          return "Width";
        case 4:
          return "Height";
        case 5:
          return "Color";
        default:
          return super.getColumnName(column);
      }
    }

    @Override
    public int getRowCount() {
      return (keyframes != null ? keyframes.size() : 0);
    }

    @Override
    public int getColumnCount() {
      return 6;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      Keyframe keyFrame = keyframes.get(rowIndex);
      switch (columnIndex) {
        case 1:
          controller.editKeyframe(keyFrame.getTime(), "x", aValue);
          break;
        case 2:
          controller.editKeyframe(keyFrame.getTime(), "y", aValue);
          break;
        case 3:
          controller.editKeyframe(keyFrame.getTime(), "width", aValue);
          break;
        case 4:
          controller.editKeyframe(keyFrame.getTime(), "height", aValue);
          break;
        case 5:
          controller.editKeyframe(keyFrame.getTime(), "color", aValue);
          break;
        default:
          throw new IllegalStateException("Unrecognized edit in Kayframe Table");
      }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      Keyframe keyframe = keyframes.get(rowIndex);
      switch (columnIndex) {
        case 0:
          return keyframe.getTime();
        case 1:
          return keyframe.getPosition().x;
        case 2:
          return keyframe.getPosition().y;
        case 3:
          return keyframe.getWidth();
        case 4:
          return keyframe.getHeight();
        case 5:
          return keyframe.getColor();
        default:
          return null;
      }
    }
  }

  /**
   * Represents a renderer for Colors in a JTable.
   */
  private class ColorRendererCell extends JLabel implements TableCellRenderer {

    /**
     * Constructs a color renderer object.
     */
    ColorRendererCell() {
      this.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
      Color color = (Color) value;
      this.setBackground(color);
      return this;
    }
  }

  /**
   * Represents a Color Picker for a JTable.
   */
  private class ColorPicker extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private int row;
    private int col;
    private Color color;
    private JButton edit;
    private JColorChooser colorChooser;
    private JDialog window;
    private TableModelListener listener;
    private TableModel model;

    /**
     * Constructs a Color-picker.
     *
     * @param model    model for table
     * @param listener listener to respond to events
     * @param row      row number
     * @param col      column number
     */
    ColorPicker(TableModel model, TableModelListener listener, int row, int col) {
      this.model = model;
      this.row = row;
      this.col = col;
      this.listener = listener;
      edit = new JButton();
      edit.addActionListener(this);
      edit.setActionCommand("choose color");

      colorChooser = new JColorChooser();
      window = JColorChooser.createDialog(edit, "Choose Color", true, colorChooser, this, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand().equals("choose color")) {
        colorChooser.setColor(color);
        window.setVisible(true);
      } else {
        color = colorChooser.getColor();
        model.setValueAt(color, row, col);
      }
    }

    @Override
    public Object getCellEditorValue() {
      return color;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
      color = (Color) value;
      return edit;
    }
  }
}
