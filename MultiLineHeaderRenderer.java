/**
 *
 * @author Matthew Kelly
 */
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

//this class simply allows a table row/header to me multi-lined
//it is adapted from the SWING docs

public class MultiLineHeaderRenderer extends JList implements TableCellRenderer {
  public MultiLineHeaderRenderer() {
    setOpaque(true);
    setForeground(UIManager.getColor("TableHeader.foreground"));
    setBackground(UIManager.getColor("TableHeader.background"));
    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    ListCellRenderer renderer = getCellRenderer();
    ((JLabel)renderer).setHorizontalAlignment(JLabel.CENTER);
    setCellRenderer(renderer);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
    setFont(table.getFont());
    String str = (value == null) ? "" : value.toString();
    BufferedReader br = new BufferedReader(new StringReader(str));
    String line;
    Vector v = new Vector();
    try {
      while ((line = br.readLine()) != null) {
        v.addElement(line);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    setListData(v);
    return this;
  }
}
