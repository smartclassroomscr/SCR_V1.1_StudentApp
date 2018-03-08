/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.awt.image.BufferedImage;

/**
 *
 * @author Hamid
 */
public class Constants {

    public static String StudentName;
    public static String StudentId;
    //static String ServerDBIP = "192.168.0.2";
    public static String ServerDBIP = "localhost";
    //static String ServerIP = "192.168.0.2";
    public static String ServerIP = "localhost";
    
    public static int ServerDBPort = 1433;
    public static int ServerPort = 8006;

//   public static String TeacherServerIP;
//   public static int TeacherServerPort ;
//   
//    public static int ClassSession_Id;
//    public static String TeacherMulticastADDRESS;
//    public static int TeacherMulticastPORT;
//    public static String StudentMulticastADDRESS;
//    public static int StudentMulticastPORT;
    public static StartedSessionDetails StartedSessionDetails;
            
            
    public static int BUFFER_SIZE = 4000;

    public static int COLOUR_OUTPUT = BufferedImage.TYPE_INT_RGB;
    public static int Full_PACKATE_SIZE = 65507;
    public static int MY_HEADER_SIZE = 3;
    public static int Image_PAKATE_SIZE = Full_PACKATE_SIZE - MY_HEADER_SIZE;

}
