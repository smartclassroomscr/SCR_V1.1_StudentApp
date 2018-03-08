/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import Student.ScreenSharing.ScreenSharing;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import Student.SolveQuiz.ReadQuiz;
import Student.SolveQuiz.ShowQuizMain;

/**
 *
 * @author Hamid
 */
public class ClassSession_Student implements Observer {

    JTextField userMessage;
    JTextArea chatArea;
    JFrame jFrameObj;
    ScreenSharing screenSharingObj;
    ConnectToServer connectToServerObj;
    JButton chooseFile;
    JButton screenShare;
    JButton raiseHand;
    JButton changeScreenSize;

    JPanel chatPanel;
    JScrollPane chatAreaScrollPane;

    ReadQuiz readQuizObj;
    ShowQuizMain showQuizMainObj;

    ClassSession_Student() {
        jFrameObj = new JFrame();
        jFrameObj.setLayout(null);
        jFrameObj.setResizable(false);
        jFrameObj.setSize(350, 550);
        jFrameObj.setLocationRelativeTo(null);
        jFrameObj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrameObj.setForeground(Color.DARK_GRAY);
        jFrameObj.getContentPane().setBackground(Color.WHITE);

        screenShare = new JButton("Screen");
        screenShare.setForeground(Color.DARK_GRAY);
        screenShare.setBackground(Color.WHITE);
        screenShare.setBounds(5, 5, 100, 30);
        screenShare.setEnabled(false);

        raiseHand = new JButton("Raise Hand");
        raiseHand.setForeground(Color.DARK_GRAY);
        raiseHand.setBackground(Color.WHITE);
        raiseHand.setBounds(115, 5, 100, 30);

        changeScreenSize = new JButton("");
        //userMessage.setPreferredSize(new Dimension(100, 50));
        changeScreenSize.setForeground(Color.DARK_GRAY);
        changeScreenSize.setBackground(Color.WHITE);
        changeScreenSize.setBounds(215, 5, 10, 30);
        changeScreenSize.setEnabled(false);

        chatPanel = new JPanel();
        //chatPanel.setPreferredSize(new Dimension(100, 400));
        chatPanel.setBackground(Color.yellow);
        chatPanel.setLayout(null);

        userMessage = new JTextField();
        //userMessage.setPreferredSize(new Dimension(100, 50));
        userMessage.setForeground(Color.DARK_GRAY);
        userMessage.setBackground(Color.WHITE);
        //usermessage.setEditable(false);
        userMessage.setEditable(true);
        userMessage.setBounds(10, 320, 270, 50);

        chooseFile = new JButton("File");
        //userMessage.setPreferredSize(new Dimension(100, 50));
        chooseFile.setForeground(Color.DARK_GRAY);
        chooseFile.setBackground(Color.WHITE);
        chooseFile.setBounds(10, 375, 80, 20);

        chatArea = new JTextArea();
        chatAreaScrollPane = new JScrollPane(chatArea);
        chatArea.setWrapStyleWord(true);
        chatArea.setLineWrap(true);
        //chatArea.setPreferredSize(new Dimension(300, 500));
        //chatArea.setBounds(20, 20, 260, 250);
        chatAreaScrollPane.setBounds(10, 10, 270, 300);

        screenShare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                screenSharingObj.startReceivingScreen();
            }
        });
        raiseHand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                connectToServerObj.sendMessage("!@#@!_SCREEN!@#@!" + "Student!@#@!REQUEST" + "!@#@!");
                connectToServerObj.messageReceived("You Screen Sharing request is send to Teacher.");
                connectToServerObj.messageReceived("Waiting for responce.");

            }
        });
        chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(ClassSession_Student.class.getName()).log(Level.SEVERE, null, ex);
                }

                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
//                fileChooser.setCurrentDirectory(new File(("C:\\Users\\Hamid\\Downloads\\BSSE-7A\\Data Visualization")));
                int result = fileChooser.showOpenDialog(jFrameObj);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String ServerUrl = "http://" + Constants.ServerIP + ":" + Constants.ServerPort + "/FileUpload/LectureFile";
                    try {
                        ///open file
                        File myFile = new File(selectedFile.getAbsolutePath());
                        Desktop.getDesktop().open(myFile);
                        //return file name
                        String status = UploadFile.UploadFile(ServerUrl, fileChooser.getSelectedFile().toString().trim());
                        System.out.println("File Uploading RESULT:" + status);
                        //if file upoaded then update file info to online server
                        if (!status.equalsIgnoreCase("ERROR")) {
                            boolean status2 = UploadFile.UploadFileDetailsInDB(Constants.StartedSessionDetails.getClassSessionId(), status, Constants.StudentName);
                            if (status2) {
                                System.out.println("FileUploaded");
                                connectToServerObj.sendMessage("!@#@!_FILE!@#!" + status + "!@#@!");
                            } else {
                                System.out.println("ERROR:Not Able to upload File Info");
                            }
                        } else {
                            System.out.println("ERROR:Not Able to upload File.");
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(ClassSession_Student.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ClassSession_Student.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });
        userMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                connectToServerObj.sendMessage(ae.getActionCommand());
                connectToServerObj.messageReceived(Constants.StudentName + ":" + ae.getActionCommand());
                userMessage.setText("");
            }
        });
        changeScreenSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (screenSharingObj.isSendScreenAllowed()) {
                    Object[] possibleValues = {"Change Screen Size", "High Resolution", "Low Resolution"};
                    String selectedValue = (String) JOptionPane.showInputDialog(null, "Choose Option", "Screen Sharing", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
                    //again check screen share becz after clicking screnn receiving can be turn off by teacher
                    if (selectedValue != null && !selectedValue.isEmpty() && screenSharingObj.isSendScreenAllowed() && selectedValue.trim().equals("Change Screen Size")) {
                        screenSharingObj.ChangeScreenSize();
                    } else if (selectedValue != null && !selectedValue.isEmpty() && screenSharingObj.isSendScreenAllowed() && selectedValue.trim().equals("High Resolution")) {
                        screenSharingObj.changeResolution(selectedValue);
                    } else if (selectedValue != null && !selectedValue.isEmpty() && screenSharingObj.isSendScreenAllowed() && selectedValue.trim().equals("High Resolution")) {
                        screenSharingObj.changeResolution(selectedValue);
                    }
                }
            }
        });

        chatPanel.add(chatAreaScrollPane);
        chatPanel.add(userMessage);
        chatPanel.add(chooseFile);
        chatPanel.setBounds(10, 100, 300, 400);

        jFrameObj.add(screenShare);
        jFrameObj.add(raiseHand);
        jFrameObj.add(changeScreenSize);
        jFrameObj.add(chatPanel);

        jFrameObj.setVisible(true);

    }

    ClassSession_Student(ConnectToServer connectToServerObj, ScreenSharing screenSharingObj) throws UnknownHostException {
        this();
        this.connectToServerObj = connectToServerObj;
        this.screenSharingObj = screenSharingObj;
    }

    @Override
    public void update(Observable o, Object objectObj) {
        try {
            String message = (String) objectObj;
            checkMessageType(message);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ClassSession_Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkMessageType(String message) throws SQLException, IOException {
//        System.out.println("me"+message);
        if (message.contains("!@#@!")) {
            StringTokenizer stringTokenizer = new StringTokenizer(message, "!@#@!");
            String action = stringTokenizer.nextToken().toString().trim();
            /* while (stringTokenizer.hasMoreTokens()) {
             System.out.println( stringTokenizer.nextToken());
             }*/
            // System.out.println(message.substring(14));
            //System.out.println(stringTokenizer.nextToken());//Teacher: 
//            System.out.println(mzessage);
            if (action.equals("_FILE")) {
                String fileName = stringTokenizer.nextToken();
                String ServerUrl = "http://" + Constants.ServerIP + ":" + Constants.ServerPort + "/Files/LectureFiles/" + fileName;
                String home = System.getProperty("user.home");

                String saveDir = home + "/Downloads/";
                try {
                    if (DownloadFile.downloadFile(ServerUrl, saveDir)) {
                        OpenFile.openFile(saveDir + fileName);
                        System.out.println("File Downloaded");
                    } else {
                        System.out.println("Error In Downloading File");
                    }
                    System.out.println("Uploaded by:" + stringTokenizer.nextToken());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (action.equals("_BROWSER")) {
                try {
                    String url = stringTokenizer.nextToken();
                    System.out.println(url);
                    URI myUri = URI.create(url);
                    java.awt.Desktop.getDesktop().browse(myUri);
                } catch (IOException ex) {
                    Logger.getLogger(ClassSession_Student.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (action.equals("_PAINT")) {
                try {
                    Runtime.getRuntime().exec("mspaint.exe");
                } catch (IOException ex) {
                    Logger.getLogger(ConnectToServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (action.equals("_SCREEN")) {
                String screenActor = stringTokenizer.nextToken();
                String screenStatus = stringTokenizer.nextToken();
                if (screenActor.equalsIgnoreCase("Teacher") && screenStatus.trim().equalsIgnoreCase("ON")) {
                    screenShare.setEnabled(true);
                    screenSharingObj.setReceiveScreenAllowed(true);
                    screenSharingObj.startReceivingScreen(stringTokenizer.nextToken());
                } else if (screenActor.equalsIgnoreCase("Teacher") && screenStatus.trim().equalsIgnoreCase("OFF")) {
                    screenShare.setEnabled(false);
                    screenSharingObj.setReceiveScreenAllowed(false);
                    screenSharingObj.stopReceivingScreen();
                } else if (screenActor.equalsIgnoreCase("Student") && screenStatus.trim().equalsIgnoreCase("PORT")) {
                    changeScreenSize.setEnabled(true);
                    screenSharingObj.findMultiCastPort();
                    screenSharingObj.setSendScreenAllowed(true);
                    screenSharingObj.startSendingScreen();
                    connectToServerObj.sendMessage("!@#@!_SCREEN!@#@!" + "Student!@#@!PORT" + "!@#@!" + Constants.StartedSessionDetails.getStudentMulticastPort());
                } else if (screenActor.equalsIgnoreCase("Student") && screenStatus.trim().equalsIgnoreCase("OFF")) {
                    changeScreenSize.setEnabled(false);
                    screenSharingObj.setReceiveScreenAllowed(false);
                    screenSharingObj.stopSendingScreen();
                }
            } else if (action.equals("_QUIZ")) {
                String quizAction = stringTokenizer.nextToken();
                String quizId = stringTokenizer.nextToken();
                if (quizAction.trim().equalsIgnoreCase("TakeQuiz")) {
                    readQuizObj = new ReadQuiz();
                    readQuizObj.readQuizDetails(Integer.parseInt(quizId));
                    readQuizObj.readQuestions();
                    showQuizMainObj = new ShowQuizMain(readQuizObj.getQuizObj());
                }
            } else if (action.equals("_REMOVED")) {
                chatArea.append("\n" + "You have been removed from class session by " + stringTokenizer.nextToken());
                this.connectToServerObj.closeConnection();
            } else if (action.equals("_REJECTED")) {
                chatArea.append("\n" + stringTokenizer.nextToken() + " rejected your connection request.");
                this.connectToServerObj.closeConnection();
            }

        } else {
            showMessage(message);

        }

    }

    public void showMessage(String message) {
        chatArea.append("\n" + message);

    }

    ClassSession_Student(boolean srartServer) throws InterruptedException, IOException {
        try {
            screenSharingObj = new ScreenSharing();
//            screenSharingObj.startSendingScreen();

            ConnectToServer connectToServerObj = new ConnectToServer();
            ClassSession_Student mainWindow = new ClassSession_Student(connectToServerObj, screenSharingObj);
            Thread queryThread = new Thread(connectToServerObj);
            queryThread.start();
            connectToServerObj.addObserver(mainWindow);

        } catch (UnknownHostException ex) {
            Logger.getLogger(ClassSession_Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) throws InterruptedException, IOException, SQLException {
//        Thread queryThread = new Thread() {
//            public void run() {
//                try {
//                    System.out.println("System Started");
//                    ConnectToDB.connectToDB();
//
//                    Constants.StudentName = "ghuffran";
//                    Constants.StudentId = "1";
////
////        Constants.StudentName = "turab";
////        Constants.StudentId = "2";
//
//                    MainWindow_Student mainWindow = new MainWindow_Student();
//                    mainWindow.getjFrame().dispose();
//                    ReadClassSessionsDetails readClassSessionsDetailsObj = new ReadClassSessionsDetails();
//                    mainWindow.setClassSessionArrayList(readClassSessionsDetailsObj.readTeacherClassSessionsDetails());
//
//                    StartedSessionDetails startedSessionDetailsObj
//                            = new StartedSessionDetails(mainWindow.getClassSessionArrayList().get(0));
//                    Constants.StartedSessionDetails = startedSessionDetailsObj;
//                    ClassSession_Student ClassSession_Student = new ClassSession_Student(true);
//                } catch (SQLException | InterruptedException | IOException ex) {
//                    Logger.getLogger(ClassSession_Student.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        };
//        queryThread.start();

//         Thread queryThread2 = new Thread() {
//            public void run() {
        try {
            System.out.println("System Started");
            ConnectToDB.connectToDB();

//                    Constants.StudentName = "ghuffran";
//                    Constants.StudentId = "1";

            Constants.StudentName = "turab";
            Constants.StudentId = "2";

            MainWindow_Student mainWindow = new MainWindow_Student();
            mainWindow.getjFrame().dispose();
            ReadClassSessionsDetails readClassSessionsDetailsObj = new ReadClassSessionsDetails();
            mainWindow.setClassSessionArrayList(readClassSessionsDetailsObj.readTeacherClassSessionsDetails());

            StartedSessionDetails startedSessionDetailsObj
                    = new StartedSessionDetails(mainWindow.getClassSessionArrayList().get(0));
            Constants.StartedSessionDetails = startedSessionDetailsObj;
            ClassSession_Student ClassSession_Student = new ClassSession_Student(true);
        } catch (SQLException | InterruptedException | IOException ex) {
            Logger.getLogger(ClassSession_Student.class.getName()).log(Level.SEVERE, null, ex);
        }
//            }
//        };
//        queryThread2.start();

    }
}
