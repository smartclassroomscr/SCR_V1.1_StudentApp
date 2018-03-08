/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.awt.AWTException;
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
import java.util.Observable;
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
public class ConnectToServer extends Observable implements Runnable {

    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    Socket connect;

    boolean socketConnected;

    @Override
    public void run() {
        startProcess();
    }

    //connect to server
    public void startProcess() {
        try {

            socketConnected = true;
            //connect to server
            messageReceived("Trying to connect");
            //connect = new Socket(InetAddress.getByName(serverIP), 5070);
//            connect = new Socket(InetAddress.getByName(Constants.TeacherServerIP), Constants.TeacherServerPort);
//            messageReceived("Connected To Teacher:" + connect.getInetAddress().getHostName());
            String[] separated = Constants.StartedSessionDetails.getTeacherIpAddress().split(":");
//            connect = new Socket(InetAddress.getByName(separated[0]), Integer.parseInt(separated[1]));
            connect = new Socket(InetAddress.getByName(separated[0]), Integer.parseInt(separated[1]));
            messageReceived("Connected To Teacher");
//            messageReceived("Connected To Teacher:" + connect.getInetAddress().getHostName());

            //set streams
            outputStream = new ObjectOutputStream(connect.getOutputStream());
            outputStream.flush();
            sendMessage("!@#@!_ID_NAME!@#!" + Constants.StudentId + "!@#!" + Constants.StudentName);
            inputStream = new ObjectInputStream(connect.getInputStream());

            //workingshowMessage("Streams Created", 1);
            ///while chatting
            String message = "";
            // showMessage("Connected.");
//            SwingUtilities.invokeLater(
//                    new Runnable() {
//
//                        @Override
//                        public void run() {
//                            userMessage.setEditable(true);
//                        }
//                    });

            ///chatting
            do {
                try {
                    message = (String) inputStream.readObject();
                    messageReceived(message);

                } catch (Exception ex) {

                    messageReceived("Wrong Input..");
                    //showMessage("Server Stop", 1);
                    message = ("SERVER  - END");
                                socketConnected = false;

                    // System.exit(0);
                }
            } while (socketConnected);
//            } while (!message.equals("SERVER  - END") || socketConnected );

        } catch (Exception ex) {
            messageReceived("Error  in connecting Server");
        } finally {
            //close Every thing
            messageReceived("Closing Connection.");
//            SwingUtilities.invokeLater(
//                    new Runnable() {
//
//                        @Override
//                        public void run() {
//                            userMessage.setEditable(false);
//                        }
//                    });
            closeConnection();

        }
    }

    public void closeConnection() {

        try {
            socketConnected = false;

            outputStream.close();
            inputStream.close();
            connect.close();

        } catch (Exception ex) {
//            messageReceived("Wrong Input.");
        }
    }

    //send message to Server
    public void sendMessage(String message) {

        try {
            outputStream.writeObject(message);
            outputStream.flush();

        } catch (Exception ex) {
            messageReceived("Message Cannot Be Sent Becz of Error");
        }

    }

    //show message in gui
    public void messageReceived(String message) {
        setChanged();
        notifyObservers(message);
    }

}
