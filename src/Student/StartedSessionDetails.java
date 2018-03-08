/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hamid
 */
public class StartedSessionDetails extends ClassSessionDetails {

    private int teacherMulticastPort;
    private int studentMulticastPort;


    StartedSessionDetails(ClassSessionDetails classSessionDetailsObj) {
        super(classSessionDetailsObj);
        studentMulticastPort = 4002;
    }

    public int getTeacherMulticastPort() {
        return teacherMulticastPort;
    }

    public void setTeacherMulticastPort(int teacherMulticastAddressPort) {
        this.teacherMulticastPort = teacherMulticastAddressPort;
    }

    public int getStudentMulticastPort() {
        return studentMulticastPort;
    }

    public void setStudentMulticastPort(int studentMulticastAddressPort) {
        this.studentMulticastPort = studentMulticastAddressPort;
    }

    public String toString() {
        return super.toString() + teacherMulticastPort + ":" + studentMulticastPort ;

    }
}
