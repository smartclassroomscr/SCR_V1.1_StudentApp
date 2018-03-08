/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import com.sun.image.codec.jpeg.ImageFormatException;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;

/**
 *
 * @author Hamid
 */
public class ChangeScreenSize {

    JFrame frame;
    boolean changeSize = true, press = true, run = true;
    int count = 0;
    int left, top, right, bottom;

    public static void main(String[] args) throws AWTException, ImageFormatException, IOException {
        new ChangeScreenSize().getScreenSize();;

    }

    ChangeScreenSize() {
        frame = new JFrame("Multicast Image Sender");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        frame.setUndecorated(true);
        frame.getContentPane().setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        frame.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(8, 8, 8, 8, Color.RED));

        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                press = false;

            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

    }

    public Rectangle getScreenSize() {

        frame.setResizable(true);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        Rectangle rectangleTemp = new Rectangle();

        while (run) {
            PointerInfo p = MouseInfo.getPointerInfo();
            int xx = p.getLocation().x;
            int yy = p.getLocation().y;

            if (changeSize == true && count == 0 && press) {
                frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                Rectangle obj = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

                left = xx - 4;
                top = yy - 4;

                frame.setBounds(left, top, ((int) obj.getWidth()), (int) obj.getHeight());
                frame.setVisible(true);
                if (!press) {
                    count = 1;
                    press = true;
                }

            } else if (changeSize && count == 1 && press) {
                Rectangle obj = new Rectangle(frame.getBounds());

                right = xx - left + 4;
                bottom = yy - top + 4;

                frame.setBounds(left, top, right, bottom);
                frame.setVisible(true);

                if (!press) {
                    count = 2;
                    press = true;
                }
            } else {
                run = false;
            }
            try {

            } catch (Exception ex) {
                Logger.getLogger(ChangeScreenSize.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        rectangleTemp = frame.getBounds();
        System.out.println("New Coordinates Are:" + frame.getBounds());

        Thread queryThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (Exception ex) {
                    Logger.getLogger(ChangeScreenSize.class.getName()).log(Level.SEVERE, null, ex);
                }
                frame.setVisible(false);
            }
        };
        queryThread.start();

        return rectangleTemp;
    }
}
