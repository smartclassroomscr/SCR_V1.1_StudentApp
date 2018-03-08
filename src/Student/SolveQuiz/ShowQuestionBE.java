/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student.SolveQuiz;

/**
 *
 * @author Hamid
 */
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @version
 */
public class ShowQuestionBE extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    private int clickedRow;

    ShowQuizMain showQuizMainObj;

    public ShowQuestionBE(JCheckBox checkBox, ShowQuizMain showQuizMainObj) {
        super(checkBox);
        button = new JButton();
        this.showQuizMainObj = showQuizMainObj;
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    fireEditingStopped();
                } catch (Exception ex) {
                    Logger.getLogger(ShowQuestionBE.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        clickedRow = row;
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            try {
                showQuizMainObj.getjFrame().dispose();
//                showQuizMainObj.setSelected(clickedRow);
                showQuizMainObj.getQuizObj().setSelected(clickedRow);
                QuestionGui obj = new QuestionGui(showQuizMainObj.getQuizObj());
            } catch (IOException ex) {
                Logger.getLogger(ShowQuestionBE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        isPushed = false;
        return new String(label);
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        try {
            super.fireEditingStopped();
        } catch (Exception ex) {
            // Logger.getLogger(ShowQuestionBE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
