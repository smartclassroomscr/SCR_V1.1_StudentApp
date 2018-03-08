/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveQuiz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Hamid
 */
public class QuestionGui {

    private Answer questionObj;

    private JFrame jFrame;
    private JLabel questionNoLabel;
    private JLabel marksLabel;
    private JTextArea questionTextArea;
    private JScrollPane questionTextAreaScroll;

    private JButton previousButton;
    private JButton nextButton;
    private JButton homeButton;

    private MCQsBased answerPanelMCQs;
    private OpenEndedBased answerPanelOpenEnded;
    private JScrollPane answerPanelOpenEndedScroll;

    private JLabel remainingTimeLabel;
    private JLabel showRemainingTimeLabel;
    private JLabel questionTimeLabel;
    private JLabel showQuestionTimeLabel;
    Timer questionTimer;

    Quiz quizObj;

    QuestionGui() throws IOException {

        questionObj = new Answer();
        questionObj.setAnswerType("Mcqs");

        jFrame = new JFrame();
        jFrame.setLayout(null);

        questionNoLabel = new JLabel("Question:");
        questionNoLabel.setBounds(50, 50, 100, 30);

        questionTextArea = new JTextArea();
//        questionTextArea.setColumns(30);
//        questionTextArea.setRows(20);
        questionTextArea.setEditable(false);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setLineWrap(true);
        questionTextAreaScroll = new JScrollPane(questionTextArea,
                //JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        questionTextAreaScroll.setBounds(135, 50, 600, 100);

        marksLabel = new JLabel("Marks:");
        marksLabel.setBounds(735, 50, 100, 30);

        answerPanelMCQs = (MCQsBased) new MCQsBased();
//        answerPanelMCQs.setBounds(40, 200, 550, 220);
//        answerPanelMCQs.setBackground(Color.YELLOW);
//
        answerPanelOpenEnded = new OpenEndedBased();
        answerPanelOpenEndedScroll = new JScrollPane(answerPanelOpenEnded, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        previousButton = new JButton("Previous Question");
        previousButton.setBounds(140, 480, 140, 30);

        homeButton = new JButton("Main Page");
        homeButton.setBounds(300, 480, 140, 30);

        nextButton = new JButton("Next Question");
        nextButton.setBounds(460, 480, 140, 30);

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (quizObj.getSelected() > 0) {
                    try {
                        //first save the data
                        saveQuestionAnsewer();
                        //then open other question
                        questionTimer.stop();
                        jFrame.dispose();

                        quizObj.setSelected((quizObj.getSelected() - 1));
                        QuestionGui obj = new QuestionGui(quizObj);
                    } catch (Exception ex) {
                        Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    //first save the data
                    saveQuestionAnsewer();
                    //then open other question
                    questionTimer.stop();
                    jFrame.dispose();

                    quizObj.setSelected((-1));

                    ShowQuizMain obj = new ShowQuizMain(quizObj);
                } catch (Exception ex) {
                    Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (quizObj.getSelected() < quizObj.getQuestionsArrayList().size() - 1) {
                    try {
                        //first save the data
                        saveQuestionAnsewer();
                        //then open other question
                        questionTimer.stop();
                        jFrame.dispose();

                        quizObj.setSelected((quizObj.getSelected() + 1));

                        QuestionGui obj = new QuestionGui(quizObj);
                    } catch (Exception ex) {
                        Logger.getLogger(ShowQuizMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        remainingTimeLabel = new JLabel("Remaining Time:");
        remainingTimeLabel.setBounds(135, 25, 100, 30);

        showRemainingTimeLabel = new JLabel("");
        showRemainingTimeLabel.setBounds(235, 25, 50, 30);

        questionTimeLabel = new JLabel("Question Time:");
        questionTimeLabel.setBounds(400, 25, 100, 30);

        showQuestionTimeLabel = new JLabel("");
        showQuestionTimeLabel.setBounds(500, 25, 50, 30);

        jFrame.add(remainingTimeLabel);
        jFrame.add(showRemainingTimeLabel);
        jFrame.add(questionTimeLabel);
        jFrame.add(showQuestionTimeLabel);
        jFrame.add(questionNoLabel);
        jFrame.add(questionTextAreaScroll);
        jFrame.add(marksLabel);
        jFrame.add(answerPanelMCQs);
        jFrame.add(answerPanelOpenEndedScroll);
        jFrame.add(previousButton);
        jFrame.add(homeButton);
        jFrame.add(nextButton);

        jFrame.setSize(1000, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("Define Quiz");
        jFrame.setVisible(true);

    }

    QuestionGui(Quiz quizObj) throws IOException {
        this();
        this.quizObj = quizObj;
        this.questionObj = quizObj.getQuestionsArrayList().get(quizObj.getSelected());

        questionNoLabel.setText(questionNoLabel.getText() + "" + questionObj.getQuestionNo());
        marksLabel.setText(marksLabel.getText() + "" + questionObj.getMarks());
        questionTextArea.setText(questionObj.getQuestion());
        showRemainingTimeLabel.setText(quizObj.convertSecToMinute(quizObj.getRemainingTime()));
        showQuestionTimeLabel.setText(quizObj.convertSecToMinute(quizObj.getRemainingTime()));

        questionTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRemainingTimeLabel.setText(quizObj.convertSecToMinute(quizObj.getRemainingTime()));
                showQuestionTimeLabel.setText(quizObj.convertSecToMinute(questionObj.getQuestionTime()));
                questionObj.setQuestionTime((questionObj.getQuestionTime() + 1));
                if (quizObj.getRemainingTime() <= 1) {
                    try {
                        saveQuestionAnsewer();
                        questionTimer.stop();
                        jFrame.dispose();
//                        selected = -1;
                        quizObj.setSelected(-1);

                        ShowQuizMain obj = new ShowQuizMain(quizObj);
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(QuestionGui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        questionTimer.start();

        if (questionObj.getAnswerType().equalsIgnoreCase("Mcqs")) {
            answerPanelMCQs.setVisible(false);
            if (questionObj.getImage()) {
                answerPanelMCQs = new MCQsBased(questionObj.getAllOptions(), questionObj.getStudentAnswer(), questionObj.getImageUrl());
            } else {
                answerPanelMCQs = new MCQsBased(questionObj.getAllOptions(), questionObj.getStudentAnswer());
            }
            answerPanelMCQs.setBackground(Color.YELLOW);
            answerPanelMCQs.setBounds(50, 170, 757, 300);
            jFrame.add(answerPanelMCQs);
            jFrame.setVisible(true);
        } else {
            answerPanelMCQs.setVisible(false);

            if (!questionObj.getImage() && !questionObj.getBoard()) {
                answerPanelOpenEnded = new OpenEndedBased(questionObj.getMaximumWords(), questionObj.getStudentAnswer());
                answerPanelOpenEnded.setPreferredSize(new Dimension(757, 290));
            } else if (questionObj.getImage() && !questionObj.getBoard()) {
                answerPanelOpenEnded = new OpenEndedBased(questionObj.getMaximumWords(), questionObj.getStudentAnswer(), questionObj.getImageUrl());
                answerPanelOpenEnded.setPreferredSize(new Dimension(1000, 650));
            } else if (!questionObj.getImage() && questionObj.getBoard()) {
                answerPanelOpenEnded = new OpenEndedBased(questionObj.getMaximumWords(), questionObj.getStudentAnswer(), questionObj.getBoard(), questionObj.getQuestionNo());
                answerPanelOpenEnded.setPreferredSize(new Dimension(1000, 650));
            } else if (questionObj.getImage() && questionObj.getBoard()) {
                answerPanelOpenEnded = new OpenEndedBased(questionObj.getMaximumWords(), questionObj.getStudentAnswer(), questionObj.getImageUrl(),
                        questionObj.getBoard(), questionObj.getQuestionNo());
                answerPanelOpenEnded.setPreferredSize(new Dimension(1000, 1000));

            }
            answerPanelOpenEnded.setBackground(Color.YELLOW);
            answerPanelOpenEndedScroll = new JScrollPane(answerPanelOpenEnded, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            answerPanelOpenEndedScroll.setBounds(50, 170, 757, 300);
            jFrame.add(answerPanelOpenEndedScroll);
            jFrame.setVisible(true);
        }
    }

    public void saveQuestionAnsewer() throws SQLException, IOException {
        if (questionObj.getAnswerType().equalsIgnoreCase("Mcqs")) {
            try {
                questionObj.setStudentAnswer(answerPanelMCQs.getAnswerTypeGroup().getSelection().getActionCommand().trim());
            } catch (Exception ex) {
                System.out.println("-1");
            }

        } else if (questionObj.getAnswerType().equalsIgnoreCase("OpenEnded")) {
            if (!answerPanelOpenEnded.getAnswerTextArea().getText().isEmpty()) {
                questionObj.setStudentAnswer(answerPanelOpenEnded.getAnswerTextArea().getText());
            } else {
                questionObj.setStudentAnswer("");
            }
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        ReadQuiz readQuizObj = new ReadQuiz();
        readQuizObj.readQuizDetails(15);
        readQuizObj.readQuestions();
//        showQuizMainObj obj = new showQuizMainObj();
//        QuestionGui obj = new QuestionGui(quizObj.getQuestionsArrayList().get(0));

//        QuestionGui obj = new QuestionGui();
    }
}
