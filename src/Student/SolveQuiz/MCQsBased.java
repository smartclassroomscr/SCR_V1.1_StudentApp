/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student.SolveQuiz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Hamid
 */
public class MCQsBased extends JPanel {

    private int totalOptions;

    private JLabel optionLabel;

    private ButtonGroup answerTypeGroup;
    private JRadioButton option1;
    private JRadioButton option2;
    private JRadioButton option3;
    private JRadioButton option4;

    JLabel imageLabel;

    MCQsBased() {
        setLayout(null);
        totalOptions = 0;
        optionLabel = new JLabel("Choose Answer");
        optionLabel.setBounds(20, 30, 150, 30);

        answerTypeGroup = new ButtonGroup();
        option1 = new JRadioButton("1");
        option1.setBounds(20, 70, 250, 30);

        option2 = new JRadioButton("2");
        option2.setBounds(20, 110, 250, 30);

        option3 = new JRadioButton("3");
        option3.setBounds(20, 150, 250, 30);

        option4 = new JRadioButton("4");
        option4.setBounds(20, 190, 250, 30);

        answerTypeGroup.add(option1);
        answerTypeGroup.add(option2);
        answerTypeGroup.add(option3);
        answerTypeGroup.add(option4);

        option1.setVisible(false);
        option2.setVisible(false);
        option3.setVisible(false);
        option4.setVisible(false);

        imageLabel = new JLabel();
        imageLabel.setVisible(false);

        add(optionLabel);
        add(option1);
        add(option2);
        add(option3);
        add(option4);
        add(imageLabel);
    }

    MCQsBased(String options, String answer) {
        this();
        String[] separated = options.split(",");
        for (String opt : separated) {
            totalOptions++;

            if (totalOptions == 1) {
                option1.setVisible(true);
                option1.setText(opt);
                option1.setActionCommand(opt);
                setSelectedAnswer(option1, opt, answer);
            } else if (totalOptions == 2) {
                option2.setVisible(true);
                option2.setText(opt);
                option2.setActionCommand(opt);
                setSelectedAnswer(option2, opt, answer);
            } else if (totalOptions == 3) {
                option3.setVisible(true);
                option3.setText(opt);
                option3.setActionCommand(opt);
                setSelectedAnswer(option3, opt, answer);
            } else if (totalOptions == 4) {
                option4.setVisible(true);
                option4.setText(opt);
                option4.setActionCommand(opt);
                setSelectedAnswer(option4, opt, answer);
            }
        }
    }

    MCQsBased(String options, String Answer, String imageUrl) {
        this(options, Answer);

        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        imageLabel.setVisible(true);
                        imageLabel.setBounds(300, 30, 390, 220);
                        imageLabel.setIcon(new ImageIcon(new ImageIcon("Temp//" + imageUrl).getImage().getScaledInstance(390, 240, Image.SCALE_DEFAULT)));
                        setVisible(true);
                    }
                });
    }

    public ButtonGroup getAnswerTypeGroup() {
        return answerTypeGroup;
    }

    public void setSelectedAnswer(JRadioButton optionRadioButton, String optionText, String answer) {

        if (!answer.isEmpty()) {
            if (answer.trim().equals(optionText)) {
                optionRadioButton.setSelected(true);
            }
        }

    }

    public static void main(String[] args) {
        MCQsBased obj = new MCQsBased("as,as,", "459167967.jpg");

        obj.setBounds(15, 15, 757, 260);
        obj.setBackground(Color.YELLOW);

        JFrame jFrame = new JFrame();
        jFrame.setLayout(null);
        jFrame.add(obj);
//        obj.imageLabel.setVisible(true);
//        obj.imageLabel.setText("asdasd");
//        obj.imageLabel.setIcon(new ImageIcon("Temp//" + "3_Simple_Rules-wallpaper-10076549.jpg"));

        jFrame.setSize(800, 350);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("Define Quiz");
        jFrame.setVisible(true);
    }
}
