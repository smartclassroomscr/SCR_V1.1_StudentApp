/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Hamid
 */
public class ReceiveScreen implements Runnable {

    JFrame frame;
    JLabel labelImage;

    int PACKATE_Serial_No = 0;

    int imageWidth;
    int imageHeight;

    JButton closeButton;

    private boolean receiveScreen;

   

    public static void main(String[] args) throws UnknownHostException, IOException {
        Constants.TeacherMulticastADDRESS = "225.4.5.6";
        Constants.TeacherServerPort = 4002;

     ReceiveScreen obj=   new ReceiveScreen();
     obj.receiveScreen=true;
        obj.startReceivingScreen();

    }

    @Override
    public void run() {
        try {
            startReceivingScreen();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public boolean isReceiveScreen() {
        return receiveScreen;
    }

    public void setReceiveScreen(boolean receiveScreen) {
        this.receiveScreen = receiveScreen;
    }
    public void startReceivingScreen() throws UnknownHostException, IOException {
        labelImage = new JLabel("");
        frame = new JFrame("Screen Receiver");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(labelImage, BorderLayout.CENTER);
        frame.setSize(1200, 700);

        closeButton = new JButton("Close");
        frame.getContentPane().add(closeButton, BorderLayout.SOUTH);
        receiveScreen = true;
        frame.setVisible(true);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
              stopReceivingScreen();
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                imageWidth = c.getWidth();
                imageHeight = c.getHeight();
            }
        });

        InetAddress ia = InetAddress.getByName(Constants.TeacherMulticastADDRESS);

        MulticastSocket ms = new MulticastSocket(Constants.TeacherMulticastPORT);
        ms.joinGroup(ia);

        byte[] buffer = new byte[Constants.Full_PACKATE_SIZE];
        short previousSerialNo = -1, present = 1;
        byte[] temp = null;
        while (receiveScreen) {
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            ms.receive(dp);
            byte[] data = dp.getData();
            short packetSerialNo = (short) (data[0] & 0xff);
            short totalPackets = (short) (data[1] & 0xff);
            short packetNo = (short) (data[2] & 0xff);
            present = packetNo;
            System.out.println("Serial NO:" + packetSerialNo + " Total Pakets:" + totalPackets + " Packet No:" + packetNo);
            if (packetSerialNo == previousSerialNo) {
                if (totalPackets != (packetNo)) {
                    System.arraycopy(data, 3, temp, ((Constants.Image_PAKATE_SIZE * packetNo) - Constants.Image_PAKATE_SIZE), Constants.Image_PAKATE_SIZE);

                } else {
                    System.arraycopy(data, 3, temp, ((Constants.Image_PAKATE_SIZE * packetNo) - Constants.Image_PAKATE_SIZE), Constants.Image_PAKATE_SIZE);
                    showImage(temp);
                    System.out.println("Show Image" + packetSerialNo);
                }
            } else {
                if (totalPackets == 1) {
                    System.arraycopy(data, 3, data, 0, data.length - 3);
                    previousSerialNo = packetSerialNo;
                    showImage(data);
                } else {
                    previousSerialNo = packetSerialNo;
                    temp = new byte[totalPackets * Constants.Image_PAKATE_SIZE];
                    System.arraycopy(data, 3, temp, 0, Constants.Image_PAKATE_SIZE);
                }

            }

        }
    }

    public void showImage(byte[] data) throws IOException {
        // If image is complete dispay it 
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(
                    data);
            BufferedImage image = ImageIO.read(bis);
            image = Scalr.resize(image, imageWidth);
            labelImage.setIcon(new ImageIcon(image));
        } catch (Exception ex) {
            Logger.getLogger(ReceiveScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public void stopReceivingScreen(){
     frame.setVisible(false);
                receiveScreen = false;
    }
    
}
