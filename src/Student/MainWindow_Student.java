/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hamid
 */
public class MainWindow_Student {

    private JFrame jFrame;

    DefaultTableModel model;
    private JTable allStartedSessionsTable;
    JScrollPane allStartedSessionsTableScroll;
    String[] columns = {"No", "Course Id", "Course Title", "Course Section", "Action"
    };
    Object[][] data = {};

   private ArrayList<ClassSessionDetails> classSessionArrayList;

   
    MainWindow_Student() {
        jFrame = new JFrame("Server");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setForeground(Color.DARK_GRAY);
        jFrame.getContentPane().setBackground(Color.WHITE);
        jFrame.setResizable(false);
        jFrame.setSize(700, 500);
        jFrame.setLayout(null);

        classSessionArrayList = new ArrayList<ClassSessionDetails>();

        allStartedSessionsTable = new JTable(data, columns);
       allStartedSessionsTableScroll = new JScrollPane(allStartedSessionsTable);
       allStartedSessionsTableScroll.setBounds(50, 130, 600, 200);
        allStartedSessionsTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
        ///Dimensions are in the form of width and height of table not cells
        allStartedSessionsTable.setFillsViewportHeight(false);
        allStartedSessionsTable.setEnabled(true);//for editable or not

        allStartedSessionsTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                columns
        ) {
            boolean[] canEdit = new boolean[]{
                true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex ==4) {
                    //return canEdit[columnIndex];
                    return true;
                } else {
                    return false;
                }
            }
        });
        allStartedSessionsTable.setColumnSelectionAllowed(true);
        // jScrollPane1.setViewportView(table);
        allStartedSessionsTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (allStartedSessionsTable.getColumnModel().getColumnCount() > 0) {
            allStartedSessionsTable.getColumnModel().getColumn(0).setResizable(false);
        }
        model = (DefaultTableModel) allStartedSessionsTable.getModel();

        allStartedSessionsTable.getColumnModel().getColumn(4).setCellRenderer(new StartSessionBR());
        allStartedSessionsTable.getColumnModel().getColumn(4).setCellEditor(new StartSessionBE(new JCheckBox(), this));

        jFrame.add(allStartedSessionsTableScroll);
        jFrame.setVisible(true);

    }

    MainWindow_Student(boolean loadFirstTime) {
        this();
        try {
            ReadClassSessionsDetails readClassSessionsDetailsObj = new ReadClassSessionsDetails();
            this.classSessionArrayList = readClassSessionsDetailsObj.readTeacherClassSessionsDetails();
            loadClassSessionDetailsInTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow_Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadClassSessionDetailsInTable() {
        ((DefaultTableModel) allStartedSessionsTable.getModel()).setRowCount(0);
        for (int i = 0; i < classSessionArrayList.size(); i++) {
            model.addRow(new Object[]{i, classSessionArrayList.get(i).getCourseId(),
                classSessionArrayList.get(i).getCourseTitle(),
                classSessionArrayList.get(i).getSection(), "Join Session"});

        }
    }
     public ArrayList<ClassSessionDetails> getClassSessionArrayList() {
        return classSessionArrayList;
    }

    public void setClassSessionArrayList(ArrayList<ClassSessionDetails> classSessionArrayList) {
        this.classSessionArrayList = classSessionArrayList;
    }

      public JFrame getjFrame() {
        return jFrame;
    }


    public static void main(String args[]) throws UnknownHostException, InterruptedException, IOException {
        System.out.println("System Started");
        /*Thread queryThread = new Thread() {
         public void run() {
         ConnectToDB.connectToDB();
         }
         };
         queryThread.start();*/
        ConnectToDB.connectToDB();

//        Constants.TeacherMulticastADDRESS = "225.4.5.6";
//        Constants.TeacherMulticastPORT = 4002;

//        Constants.StudentMulticastADDRESS = "225.4.5.7";
//        Constants.StudentMulticastPORT = 4003;

//        Constants.TeacherServerIP = "127.0.0.1";
//        Constants.TeacherServerPort = 4001;

        Constants.StudentName = "ghuffran";
        Constants.StudentId = "1";
//        Constants.StudentName = "turab";
//        Constants.StudentId = "2";

//        Constants.ClassSession_Id = 1;

        
        MainWindow_Student mainWindow = new MainWindow_Student(true);

    }

}
