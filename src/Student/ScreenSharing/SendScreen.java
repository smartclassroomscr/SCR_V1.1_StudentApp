/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student.ScreenSharing;

import Student.Constants;
import com.sun.image.codec.jpeg.ImageFormatException;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Hamid
 */
public class SendScreen implements Runnable {

    private String OUTPUT_FORMAT = "jpg";
    double SCALING = 0.8;
    private int overall = 1;
    private int PACKATE_Serial_No = 0;
    private int SLEEP_MILLIS = 500;
    Rectangle screenSize;

   boolean sendScreen;

    @Override
    public void run() {
        try {
            startSendingScreen();
        } catch (AWTException ex) {
            Logger.getLogger(SendScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SendScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(SendScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startSendingScreen() throws AWTException, IOException, InterruptedException {
        sendScreen = true;

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension fullScreenSize = toolkit.getScreenSize();
        screenSize = new Rectangle(fullScreenSize);

        BufferedImage image;
        while (sendScreen) {

            image = takeScreenShot();

            image = shrink(image, SCALING);
            byte[] imageByteArray = bufferedImageToByteArray(image, OUTPUT_FORMAT);

            sendImage(imageByteArray, Constants.StartedSessionDetails.getStudentMulticastAddress(),
                    Constants.StartedSessionDetails.getStudentMulticastPort());
            Thread.sleep(SLEEP_MILLIS);

        }
    }

    public BufferedImage takeScreenShot() throws AWTException,
            ImageFormatException, IOException {

        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenSize);

        return image;
    }

    public BufferedImage shrink(BufferedImage source, double factor) {
        int w = (int) (source.getWidth() * factor);
        int h = (int) (source.getHeight() * factor);
        return scale(source, w, h);
    }

    public BufferedImage scale(BufferedImage source, int w, int h) {
        Image image = source
                .getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
        BufferedImage result = new BufferedImage(w, h, Constants.COLOUR_OUTPUT);
        Graphics2D g = result.createGraphics();
        // g.drawImage(image, 0, 0,w,h, null);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    public byte[] bufferedImageToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    private void sendImage(byte[] imageData, String multicastAddress,
            int port) throws UnknownHostException, IOException {
        byte[] cheking = null;

        InetAddress ia;
        int ttl = 2;
        ia = InetAddress.getByName(multicastAddress);
        float totalPackats = (float) Math.ceil(Float.parseFloat(imageData.length + "")
                / Float.parseFloat(Constants.Image_PAKATE_SIZE + ""));
        float additionalBytes = (float) Math.ceil(totalPackats * 3);
        totalPackats = (int) Math.ceil((Float.parseFloat(imageData.length + "") + additionalBytes)
                / Float.parseFloat(Constants.Image_PAKATE_SIZE + ""));
        //System.out.println("OverAll Packats:" + overall);
        overall++;

        MulticastSocket ms = new MulticastSocket();
        ms.setTimeToLive(ttl);
        byte[] data = new byte[Constants.Full_PACKATE_SIZE];
        int count = 1, temp = 1, startFrom = 1;
        while (count <= totalPackats) {
            data[0] = (byte) PACKATE_Serial_No;
            data[1] = (byte) totalPackats;
            data[2] = (byte) count;
            temp = Constants.Image_PAKATE_SIZE * count;
            startFrom = temp - Constants.Image_PAKATE_SIZE;
            if ((imageData.length - startFrom) >= Constants.Image_PAKATE_SIZE) {
                System.arraycopy(imageData, startFrom, data, 3, Constants.Image_PAKATE_SIZE);

                //System.out.println("Start from:" + startFrom + " Image_PAKATE_SIZE:" + Image_PAKATE_SIZE);
            } else {
                //System.out.println("last:::Start from:" + startFrom + " data length:" + (imageData.length - startFrom));
                System.arraycopy(imageData, startFrom, data, 3, (imageData.length - startFrom));

            }

            //System.out.println("Sending Packet No:" + count + " Total Packates:" + totalPackats + " Serial NO:" + PACKATE_Serial_No);
            DatagramPacket dp = new DatagramPacket(data, data.length,
                    ia, port);
            ms.send(dp);
            count++;
        }
        System.out.println("Image  No:" + PACKATE_Serial_No);
        System.out.println("------------");
        PACKATE_Serial_No++;
        if (PACKATE_Serial_No > 255) {
            PACKATE_Serial_No = 0;
        }

    }

}
