/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author Hamid
 */
public class ScreenSharing {

    private boolean sendScreenAllowed;
    private boolean receiveScreenAllowed;

    ReceiveScreen receiveScreenObj;
    private SendScreen sendScreenObj;

    ScreenSharing() throws IOException {
        sendScreenAllowed = false;
        receiveScreenAllowed = false;

        receiveScreenObj = new ReceiveScreen();
        sendScreenObj = new SendScreen();
    }

    public boolean isSendScreenAllowed() {
        return sendScreenAllowed;
    }

    public void setSendScreenAllowed(boolean sendScreenAllowed) {
        this.sendScreenAllowed = sendScreenAllowed;
    }

    public boolean isReceiveScreenAllowed() {
        return receiveScreenAllowed;
    }

    public void setReceiveScreenAllowed(boolean receiveScreenAllowed) {
        this.receiveScreenAllowed = receiveScreenAllowed;
    }

    public void startSendingScreen() {
        if (sendScreenAllowed && !sendScreenObj.sendScreen) {

            Thread queryThread = new Thread(sendScreenObj);
            queryThread.start();
        }
    }

    public void stopSendingScreen() {
        sendScreenObj.sendScreen = false;

    }

    public void ChangeScreenSize() {
        Thread queryThread = new Thread() {
            public void run() {
                sendScreenObj.screenSize = new ChangeScreenSize().getScreenSize();

            }
        };
        queryThread.start();
    }

    public void startReceivingScreen() {
        if (receiveScreenAllowed && !receiveScreenObj.isReceiveScreen()) {
            Thread queryThread = new Thread(receiveScreenObj);
            queryThread.start();
        }

    }

    public void stopReceivingScreen() {
        receiveScreenObj.stopReceivingScreen();

    }

    public void changeResolution(String resolution) {
        if (resolution != null && !resolution.isEmpty() && resolution.trim().equals("High Resolution")) {
            sendScreenObj.SCALING = 1;
        } else if (resolution != null && !resolution.isEmpty() && resolution.trim().equals("Low Resolution")) {
            sendScreenObj.SCALING = .4;

        }
    }

    public void findMultiCastPort() {

        while (true) {
            try {
                ServerSocket s = new ServerSocket(Constants.StudentMulticastPORT);
                System.out.println("Student multicast Address is:" + Constants.StudentMulticastPORT);
                s.close();
                break;
            } catch (Exception ex) {
                Constants.StudentMulticastPORT += 1;
            }
        }
    }

}
