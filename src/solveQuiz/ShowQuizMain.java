/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveQuiz;

import Student.ConnectToDB;
import Student.Constants;
import Student.Notification;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hamid
 */
public class ShowQuizMain {

    private JFrame jFrame;

    private DefaultTableModel model;
    private JTable allQuestionsTable;
    private JScrollPane allQuestionsTableScroll;
    private String[] columns = {"QuestionNo", "Question", "Marks", "ImageUrl", "AnswerType",
       "MaximumWords", "Board", "Solve Question"
    };
    private Object[][] data = {};

    private JLabel courseLabel;
    private JLabel quizNoLabel;
    private JLabel totalMarksLabel;
    private JLabel timeLabel;

    private JLabel remainingTimeLabel;
    private JLabel showRemainingTimeLabel;
    Timer quizTimer;

    //these labels will show the result from database
    private JLabel showCourseTitleLabel;
    private JLabel showQuizNoLabel;
    private JLabel showTotalMarksLabel;
    private JLabel showTimeLabel;

    private JButton previousQuestionButton;
    private JButton submitQuizButton;
    private JButton nextQuestionButton;
    private JButton cancleButton;

    private Quiz quizObj;

    ShowQuizMain() throws SQLException {

        jFrame = new JFrame();
        jFrame.setLayout(null);

        courseLabel = new JLabel("Course");
        courseLabel.setBounds(50, 50, 100, 30);

        showCourseTitleLabel = new JLabel();
        showCourseTitleLabel.setBounds(170, 50, 200, 30);

        quizNoLabel = new JLabel("Quiz No");
        quizNoLabel.setBounds(400, 50, 100, 30);

        showQuizNoLabel = new JLabel("");
        showQuizNoLabel.setBounds(520, 50, 200, 30);

        totalMarksLabel = new JLabel("Total Marks");
        totalMarksLabel.setBounds(50, 90, 100, 30);

        showTotalMarksLabel = new JLabel("");
        showTotalMarksLabel.setBounds(170, 90, 200, 30);

        timeLabel = new JLabel("Time");
        timeLabel.setBounds(400, 90, 100, 30);

        showTimeLabel = new JLabel("");
        showTimeLabel.setBounds(520, 90, 200, 30);

        remainingTimeLabel = new JLabel("Remaining Time:");
        remainingTimeLabel.setBounds(700, 90, 100, 30);

        showRemainingTimeLabel = new JLabel("");
        showRemainingTimeLabel.setBounds(800, 90, 50, 30);

        allQuestionsTable = new JTable(data, columns);
        allQuestionsTableScroll = new JScrollPane(allQuestionsTable);
        allQuestionsTableScroll.setBounds(50, 130, 900, 200);
        allQuestionsTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
        ///Dimensions are in the form of width and height of table not cells
        allQuestionsTable.setFillsViewportHeight(false);
        allQuestionsTable.setEnabled(true);//for editable or not

        allQuestionsTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                columns
        ) {
            boolean[] canEdit = new boolean[]{
                true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 7) {
                    //return canEdit[columnIndex];
                    return true;
                } else {
                    return false;
                }
            }
        });
        allQuestionsTable.setColumnSelectionAllowed(true);
        // jScrollPane1.setViewportView(table);
        allQuestionsTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (allQuestionsTable.getColumnModel().getColumnCount() > 0) {
            allQuestionsTable.getColumnModel().getColumn(0).setResizable(false);
        }

        allQuestionsTable.getColumn("Solve Question").setCellRenderer(new ShowQuestionBR());
        allQuestionsTable.getColumn("Solve Question").setCellEditor(new ShowQuestionBE(new JCheckBox(), this));

        model = (DefaultTableModel) allQuestionsTable.getModel();

        previousQuestionButton = new JButton("previous Question");
        previousQuestionButton.setBounds(50, 340, 150, 30);

        submitQuizButton = new JButton("Submit Quiz");
        submitQuizButton.setBounds(210, 340, 150, 30);

        nextQuestionButton = new JButton("next Question");
        nextQuestionButton.setBounds(370, 340, 150, 30);

        cancleButton = new JButton("Cancle");
        cancleButton.setBounds(530, 340, 150, 30);

        previousQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (quizObj.getSelected() > 0) {
                    jFrame.dispose();
                    quizObj.setSelected((quizObj.getSelected() - 1));
                    try {
                        QuestionGui obj = new QuestionGui(quizObj);
                    } catch (IOException ex) {
                        Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });
        nextQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (quizObj.getSelected() < quizObj.getQuestionsArrayList().size() - 1) {
                    try {
                        jFrame.dispose();
                        quizObj.setSelected((quizObj.getSelected() + 1));

                        QuestionGui obj = new QuestionGui(quizObj);
                    } catch (IOException ex) {
                        Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        submitQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Object[] possibleValues = {"Yes", "No"};
                String selectedValue = (String) JOptionPane.showInputDialog(null, "Are you sure you want to submit quiz?", "Submit Quiz", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
                if (selectedValue != null && !selectedValue.isEmpty() && selectedValue.trim().equals("Yes")) {
                    try {
                        //notification of uploading quiz here
                        Notification.displayNotification("Quiz", "Uploading Quiz");
                        quizTimer.stop();
                        submitQuiz();

                        jFrame.dispose();
                    } catch (AWTException ex) {
                        Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (selectedValue != null && !selectedValue.isEmpty() && selectedValue.trim().equals("No")) {
                }

            }
        });

        cancleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Object[] possibleValues = {"Yes", "No"};
                String selectedValue = (String) JOptionPane.showInputDialog(null, "Are You sure want to cancle?", "Quiz", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
                if (selectedValue != null && !selectedValue.isEmpty() && selectedValue.trim().equals("Yes")) {
                    jFrame.dispose();
                } else if (selectedValue != null && !selectedValue.isEmpty() && selectedValue.trim().equals("No")) {
                }

            }
        });

        jFrame.add(allQuestionsTableScroll);
        jFrame.add(previousQuestionButton);
        jFrame.add(nextQuestionButton);
        jFrame.add(submitQuizButton);
        jFrame.add(cancleButton);
        jFrame.add(courseLabel);
        jFrame.add(showCourseTitleLabel);
        jFrame.add(quizNoLabel);
        jFrame.add(showQuizNoLabel);
        jFrame.add(totalMarksLabel);
        jFrame.add(showTotalMarksLabel);
        jFrame.add(timeLabel);
        jFrame.add(showTimeLabel);
        jFrame.add(remainingTimeLabel);
        jFrame.add(showRemainingTimeLabel);

        jFrame.setSize(1000, 500);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("Define Quiz");
        jFrame.setVisible(true);

    }

    public ShowQuizMain(Quiz quizObj) throws SQLException {
        this();
        this.quizObj = quizObj;

        showCourseTitleLabel.setText(quizObj.getCourseTitle());
        showQuizNoLabel.setText("" + quizObj.getQuizNo());
        showTotalMarksLabel.setText("" + quizObj.getQuizTotalMarks());
        showTimeLabel.setText(quizObj.convertSecToMinute(quizObj.getQuizTime()));
        showRemainingTimeLabel.setText(quizObj.convertSecToMinute(quizObj.getRemainingTime()));

        quizTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRemainingTimeLabel.setText(quizObj.convertSecToMinute(quizObj.getRemainingTime()));
                quizObj.setRemainingTime((quizObj.getRemainingTime() - 1));
                if (quizObj.getRemainingTime() <= 1) {
                    try {
                        quizTimer.stop();
                        jFrame.dispose();
                        Notification.displayNotification("Quiz", "Uploading Quiz");

                        //notification of uploading quiz here
                        submitQuiz();
                    } catch (AWTException ex) {
                        Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        quizTimer.start();
        loadTable();
        jFrame.setVisible(true);
    }

    public void loadTable() {
        ((DefaultTableModel) allQuestionsTable.getModel()).setRowCount(0);
        for (int i = 0; i < quizObj.getQuestionsArrayList().size(); i++) {
            String tempQuestion = "Solve";
            if (!quizObj.getQuestionsArrayList().get(i).getStudentAnswer().isEmpty()) {
                tempQuestion = "Solved";
            }
            model.addRow(new Object[]{quizObj.getQuestionsArrayList().get(i).getQuestionNo(), quizObj.getQuestionsArrayList().get(i).getQuestion(),
                quizObj.getQuestionsArrayList().get(i).getMarks(), quizObj.getQuestionsArrayList().get(i).getImageUrl(), quizObj.getQuestionsArrayList().get(i).getAnswerType(),
             quizObj.getQuestionsArrayList().get(i).getMaximumWords(),
                quizObj.getQuestionsArrayList().get(i).getBoard(), tempQuestion});
        }
    }

    public void submitQuiz() {
        try {
//            System.out.println("submit quiz");
            SubmitQuiz submitQuizObj = new SubmitQuiz(this.quizObj);
            submitQuizObj.uploadQuizInDb(Constants.StudentId);
            try {
                Notification.displayNotification("Quiz", "Quiz Uploaded.");
            } catch (AWTException ex) {
                Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException | IOException ex) {
            Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Quiz getQuizObj() {
        return quizObj;
    }

    public JFrame getjFrame() {
        return jFrame;
    }

    public static void main(String[] args) throws SQLException, IOException {
        ConnectToDB.connectToDB();
        
        Constants.ClassSession_Id = 3;
     
        ReadQuiz readQuizObj = new ReadQuiz();
        readQuizObj.readQuizDetails(15);
        readQuizObj.readQuestions();
        ShowQuizMain obj = new ShowQuizMain(readQuizObj.getQuizObj());

    }

}
