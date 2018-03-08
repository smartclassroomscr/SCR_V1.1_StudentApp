/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student.SolveQuiz;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
  
/**
 * @version
 */
public class ShowQuestionBR extends JButton implements TableCellRenderer {
  
  public ShowQuestionBR() {
    setOpaque(true);
  }
   
  public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    } else{
      setForeground(table.getForeground());
      setBackground(UIManager.getColor("Button.background"));
    }
    setText( (value ==null) ? "" : value.toString() );
    return this;
  }
}
