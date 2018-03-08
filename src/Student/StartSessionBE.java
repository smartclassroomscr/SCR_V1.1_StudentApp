/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

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

/**
 * @version
 */
public class StartSessionBE extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    private int clickedRow;

    MainWindow_Student mainWindowObj;

    public StartSessionBE(JCheckBox checkBox, MainWindow_Student mainWindowObj) {
        super(checkBox);
        button = new JButton();
        this.mainWindowObj = mainWindowObj;
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    fireEditingStopped();
                } catch (Exception ex) {
                    Logger.getLogger(StartSessionBE.class.getName()).log(Level.SEVERE, null, ex);
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
//        System.out.println("clicked"+row);
        clickedRow = row;
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            try {
                mainWindowObj.getjFrame().dispose();
                StartedSessionDetails startedSessionDetailsObj
                        = new StartedSessionDetails(mainWindowObj.getClassSessionArrayList().get(clickedRow));
                Constants.StartedSessionDetails = startedSessionDetailsObj;
                ClassSession_Student mainWindow = new ClassSession_Student(true);

//                        System.out.println(mainWindowObj.getClassSessionArrayList().get(clickedRow).toString());
//
//            System.out.println("result:" + startedSessionDetailsObj.toString());
//            mainWindowObj.getAllStudentQuizesObj().setSelected(clickedRow);
//            ShowStudensAnswers showStudensAnswersObj = new ShowStudensAnswers(showAllStudentQuizesObj.getAllStudentQuizesObj());
            } catch (InterruptedException ex) {
                Logger.getLogger(StartSessionBE.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(StartSessionBE.class.getName()).log(Level.SEVERE, null, ex);
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
