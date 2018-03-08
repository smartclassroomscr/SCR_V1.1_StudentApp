/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;
/**
 *
 * @author Hamid
 */
public class Notification {

    public static void displayNotification(String title,String description) throws AWTException, MalformedURLException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
//        trayIcon.setToolTip("System tray icon demoooooo");
        tray.add(trayIcon);

        trayIcon.displayMessage(title, description, MessageType.INFO);
    }
}