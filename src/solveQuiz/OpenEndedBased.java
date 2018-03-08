/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveQuiz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Hamid
 */
public class OpenEndedBased extends JPanel {

    private JLabel answerLabel;
    private JLabel characterLabel;
    private JTextArea answerTextArea;

    private JScrollPane answerTextAreaScroll;

    private int maximumWords;

    private JLabel imageLabel;

    private QuestionBoard questionBoardObj;

    OpenEndedBased() throws IOException {
        setLayout(null);

        answerLabel = new JLabel("Answer:");
        answerLabel.setBounds(0, 5, 50, 15);

        answerTextArea = new JTextArea();
        answerTextArea.setEditable(true);
        answerTextArea.setWrapStyleWord(true);
        answerTextArea.setLineWrap(true);
        answerTextAreaScroll = new JScrollPane(answerTextArea,
                //JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        answerTextAreaScroll.setBounds(85, 5, 600, 250);

        //for not letting the user enter more charecters
        answerTextArea.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (answerTextArea.getText().length() > maximumWords - 1) {
                    e.consume();
                } else {
                    characterLabel.setText(answerTextArea.getText().length() + 1 + "/" + maximumWords);
                }
            }
        });
//for handling the copy past larger text
        answerTextArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                characterLabel.setText(answerTextArea.getText().length() + "/" + maximumWords);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (answerTextArea.getText().length() >= maximumWords + 1) {
                    SwingUtilities.invokeLater(
                            new Runnable() {
                                @Override
                                public void run() {
                                    answerTextArea.setText((answerTextArea.getText().substring(0, maximumWords)));
                                    characterLabel.setText(answerTextArea.getText().length() + "/" + maximumWords);
                                }
                            });
                }
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
            }

        });
        characterLabel = new JLabel("3000/80000");
        characterLabel.setBounds(85, 250, 150, 30);

        imageLabel = new JLabel();
        imageLabel.setVisible(false);

        add(answerLabel);
        add(characterLabel);
        add(answerTextAreaScroll);
        add(imageLabel);
    }

    OpenEndedBased(int maximumWords, String studentAnswer) throws IOException {
        this();
        this.maximumWords = maximumWords;
        answerTextArea.setText(studentAnswer);
        characterLabel.setText(answerTextArea.getText().length() + "/" + maximumWords);
    }

    OpenEndedBased(int maximumWords, String studentAnswer, String imageUrl) throws IOException {
        this(maximumWords, studentAnswer);
        SwingUtilities.invokeLater(() -> {
            imageLabel.setBounds(85, 280, 600, 350);
            imageLabel.setVisible(true);
            imageLabel.setIcon(new ImageIcon(new ImageIcon("Temp//" + imageUrl).getImage().getScaledInstance(600, 350, Image.SCALE_DEFAULT)));
            setVisible(true);
        });
    }

    OpenEndedBased(int maximumWords, String studentAnswer, boolean board, int questionNo) throws IOException {
        this(maximumWords, studentAnswer);
        SwingUtilities.invokeLater(() -> {
            try {
                questionBoardObj = new QuestionBoard("Temp//" + questionNo);
                questionBoardObj.setBackground(Color.BLACK);
                questionBoardObj.setBounds(85, 275, 600, 350);
                questionBoardObj.setVisible(board);
                add(questionBoardObj);
                setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(OpenEndedBased.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

    OpenEndedBased(int maximumWords, String studentAnswer, String imageUrl, boolean board, int questionNo) throws IOException {
        this(maximumWords, studentAnswer, board, questionNo);

        SwingUtilities.invokeLater(() -> {
            imageLabel.setBounds(85, 640, 600, 350);
            imageLabel.setVisible(true);
            imageLabel.setIcon(new ImageIcon(new ImageIcon("Temp//" + imageUrl).getImage().getScaledInstance(600, 350, Image.SCALE_DEFAULT)));
            setVisible(true);
        });
    }

    public JTextArea getAnswerTextArea() {
        return answerTextArea;
    }

    public static void main(String[] args) throws IOException {

//        OpenEndedBased obj = new OpenEndedBased(5, "459167967.jpg");
        OpenEndedBased obj = new OpenEndedBased(7, "studentAnswer", true, 1);
//        OpenEndedBased obj = new OpenEndedBased(5, "459167967.jpg", true);
        obj.setPreferredSize(new Dimension(1000, 650));
        obj.setBackground(Color.YELLOW);

        JScrollPane scrollPane = new JScrollPane(obj, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(20, 20, 757, 500);

        JFrame jFrame = new JFrame();
        jFrame.setLayout(null);
        jFrame.add(scrollPane);
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("Define Quiz");
        jFrame.setVisible(true);
    }
    
}
