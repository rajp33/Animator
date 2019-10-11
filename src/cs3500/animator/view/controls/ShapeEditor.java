package cs3500.animator.view.controls;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import cs3500.animator.model.Keyframe;
import cs3500.animator.model.ReadOnlyShape;
import cs3500.animator.view.EditorFeatures;

/**
 * Represents a panel for editing/adding/removing shapes in the AnimationEditor.
 */
public class ShapeEditor extends JPanel implements AnimationDataDisplay {
  private JTable shapes;
  private JButton addButton;
  private JButton removeButton;
  private final String[] columnNames = new String[]{"Name", "Type"};
  private EditorFeatures controller;

  /**
   * Constructs a ShapeEditor. This includes creating the table and add/remove buttons.
   */
  public ShapeEditor() {
    super();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    shapes = new ShapeTable();

    //add buttons
    addButton = new JButton("Add");
    removeButton = new JButton("Remove");
    removeButton.setActionCommand("remove");
    addButton.setActionCommand("add");

    //layout buttons
    FlowLayout flow = new FlowLayout(FlowLayout.RIGHT);
    flow.setHgap(0);
    JPanel buttonPanel = new JPanel(flow);
    buttonPanel.add(addButton);
    buttonPanel.add(removeButton);

    //add everything
    JLabel title = new JLabel("Shapes");
    title.setFont(new Font("Calibri", Font.BOLD, 16));

    this.add(title);
    this.add(buttonPanel);
    this.add(new JScrollPane(shapes));
  }

  @Override
  public void setData(Map<ReadOnlyShape, SortedSet<Keyframe>> orig) {
    String[][] data = new String[orig.keySet().size()][2];
    List<ReadOnlyShape> pairs = new ArrayList<>(orig.keySet());
    for (int i = 0; i < pairs.size(); i++) {
      ReadOnlyShape shape = pairs.get(i);
      data[i][0] = shape.getName();
      data[i][1] = shape.getType();
    }
    shapes.setModel(new DefaultTableModel(data, columnNames));
  }

  @Override
  public void addController(EditorFeatures controller) {
    this.controller = controller;
    ActionListener shapeListener = new ShapeListener(controller);

    removeButton.addActionListener(shapeListener);
    addButton.addActionListener(shapeListener);
  }

  @Override
  public void setSelected(ReadOnlyShape shape) {
    throw new UnsupportedOperationException("This method is unnecessary for a Shape Editor");
  }

  /**
   * Represents a ListSelectionListener for the shapeTable. This listener disables/enables Buttons.
   */
  private class ShapeSelectionListener implements ListSelectionListener {
    int row = -1;

    /**
     * Create a new ShapeSelectionListener.
     */
    ShapeSelectionListener() {
      super();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
      if (!e.getValueIsAdjusting()) {
        ListSelectionModel selectionModel = (ListSelectionModel) e.getSource();
        int oldValue = e.getFirstIndex();
        int newValue = e.getLastIndex();
        if (row > oldValue) {
          row = oldValue;
        } else {
          row = newValue;
        }
        if (row != shapes.getRowCount()) {
          controller.setSelectedShape((String) shapes.getValueAt(row, 0));
        }
        removeButton.setEnabled(!selectionModel.isSelectionEmpty());
      }
    }
  }

  /**
   * Represents an Action Listener that handles the Adding and removing of Shapes.
   */
  private class ShapeListener implements ActionListener {
    EditorFeatures controller;

    /**
     * Constructs a ShapeListener.
     * @param controller controller to send commands to.
     */
    ShapeListener(EditorFeatures controller) {
      super();
      this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      String actionCommand = e.getActionCommand();
      if ("remove".equals(actionCommand)) {
        controller.removeShape((String) shapes.getValueAt(shapes.getSelectedRow(), 0));
      } else if ("add".equals(actionCommand)) {
        new ShapeCreateFrame(controller);
      }
    }
  }

  /**
   * Represents a JTable used for displaying shapes.
   */
  private class ShapeTable extends JTable {

    /**
     * Constructs a new ShapeTable.
     */
    ShapeTable() {
      super();
      ListSelectionModel selectionModel = this.getSelectionModel();
      selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      selectionModel.addListSelectionListener(new ShapeSelectionListener());
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }
  }
}
