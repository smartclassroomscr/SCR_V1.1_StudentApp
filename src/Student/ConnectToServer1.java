/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Hamid
 */
public class ConnectToServer1 extends JFrame {

    JTextField userMessage;
    JTextArea chatArea;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    String message = "", serverIP = "";
    Socket connect;
    String userName;

    ConnectToServer1(String hostAddress, String userName) {
        super("Client");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setForeground(Color.DARK_GRAY);
        getContentPane().setBackground(Color.WHITE);

        serverIP = hostAddress;
        this.userName = userName;
        userMessage = new JTextField();

        userMessage.setForeground(Color.DARK_GRAY);
        userMessage.setBackground(Color.WHITE);
        userMessage.setPreferredSize(new Dimension(100, 50));
        userMessage.setEditable(false);
        userMessage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                sendMessage(ae.getActionCommand());
                showMessage(userName + ":" + ae.getActionCommand(), 1);
                userMessage.setText("");
            }
        });

        add(userMessage, BorderLayout.SOUTH);
        chatArea = new JTextArea();
        add(new JScrollPane(chatArea));
        setSize(300, 400);

        setVisible(true);

    }

    //connect to server
    public void startProcess() {
        try {
            //connect to server
            showMessage("Trying to connect", 1);
            //connect = new Socket(InetAddress.getByName(serverIP), 5070);
            connect = new Socket(InetAddress.getByName(serverIP), 4001);
            showMessage("Connected To Teacher:" + connect.getInetAddress().getHostName(), 1);

            //set streams
            outputStream = new ObjectOutputStream(connect.getOutputStream());
            outputStream.flush();
            //sendMessage("!@#@!_NAME!@#!" + userName + Math.random());
            sendMessage("!@#@!_NAME!@#!" + userName );
            inputStream = new ObjectInputStream(connect.getInputStream());

            //workingshowMessage("Streams Created", 1);
            ///while chatting
            String message = "";
            // showMessage("Connected.");
            SwingUtilities.invokeLater(
                    new Runnable() {

                        @Override
                        public void run() {
                            userMessage.setEditable(true);
                        }
                    });

            ///chatting
            do {
                try {
                    message = (String) inputStream.readObject();
                    System.out.println(message);
                    if (message.contains("!@#@!")) {
                        StringTokenizer stringTokenizer = new StringTokenizer(message, "!@#@!");
                        String action = stringTokenizer.nextToken().toString().trim();
                        /* while (stringTokenizer.hasMoreTokens()) {
                         System.out.println( stringTokenizer.nextToken());
                         }*/
                        // System.out.println(message.substring(14));
                        //System.out.println(stringTokenizer.nextToken());//Teacher: 
                        if (action.equals("_FILE")) {
                            String fileName = stringTokenizer.nextToken();
                            String ServerUrl = "http://" + Constants.ServerIP + ":" + Constants.ServerPort + "/Files/files/" + fileName;
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
                            String url = stringTokenizer.nextToken();
                            System.out.println(url);
                            URI myUri = URI.create(url);
                            java.awt.Desktop.getDesktop().browse(myUri);
                        } else if (action.equals("_PAINT")) {
                            try {
                                Runtime.getRuntime().exec("mspaint.exe");
                            } catch (IOException ex) {
                                Logger.getLogger(ConnectToServer1.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else {
                        // System.out.println("111");
                        showMessage(message, 2);

                    }

                } catch (Exception ex) {

                    showMessage("Wrong Input..", 1);
                    //showMessage("Server Stop", 1);
                    message = ("SERVER  - END");
                    // System.exit(0);
                }
            } while (!message.equals("SERVER  - END"));

        } catch (Exception ex) {
            showMessage("Error  in connecting Server", 1);
        } finally {
            //close Every thing
            showMessage("Closing Connection.", 1);
            SwingUtilities.invokeLater(
                    new Runnable() {

                        @Override
                        public void run() {
                            userMessage.setEditable(false);
                        }
                    });
            try {
                outputStream.close();
                inputStream.close();
                connect.close();

            } catch (Exception ex) {
                showMessage("Wrong Input.", 1);
            }

        }
    }

    //send message to Server
    public void sendMessage(String message) {

        try {
            outputStream.writeObject(message);
            outputStream.flush();

        } catch (Exception ex) {
            chatArea.append("Message Cannot Be Sent Becz of Error");
        }

    }

    //show message in gui
    public void showMessage(final String message, int no) {
        SwingUtilities.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {
                        chatArea.append("\n " + message);
                        /*
                         if(no==2){
                         // chatArea.setHighlighter(null);
                         Color color = JColorChooser.showDialog(chatArea, "Colors",
                         Color.BLUE);
                         chatArea.selectAll();
                         // area.setSelectedTextColor(color); // color of selected text
                         chatArea.setSelectionColor(color); // background of selected text
                         chatArea.requestFocusInWindow();
                         }*/
                    }
                });

    }

    public static void main(String args[]) {
        ConnectToServer1 clientobj = new ConnectToServer1("127.0.0.1", "Turab");
        clientobj.startProcess();

    }

}
