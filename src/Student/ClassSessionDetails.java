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
public class ClassSessionDetails {

    private int classSessionId;
    private String courseId;
    private String courseTitle;
    private String section;
    private String teacherIpAddress;
    private String teacherMulticastAddress;
    private String studentMulticastAddress;

    ClassSessionDetails() {

    }
    

    ClassSessionDetails(int classSessionId, String courseId, String courseTitle, String section,
            String ipAddress, String teacherMulticastAddress, String studentMulticastAddress) {
        this();
        this.classSessionId = classSessionId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.section = section;
        this.teacherIpAddress = ipAddress;
        this.teacherMulticastAddress = teacherMulticastAddress;
        this.studentMulticastAddress = studentMulticastAddress;
    }
    ClassSessionDetails( ClassSessionDetails classSessionDetailsObj) {
        this();
        this.classSessionId = classSessionDetailsObj.classSessionId;
        this.courseId = classSessionDetailsObj.courseId;
        this.courseTitle =classSessionDetailsObj. courseTitle;
        this.section = classSessionDetailsObj.section;
        this.teacherIpAddress =classSessionDetailsObj. teacherIpAddress;
        this.teacherMulticastAddress =classSessionDetailsObj. teacherMulticastAddress;
        this.studentMulticastAddress = classSessionDetailsObj.studentMulticastAddress;
    }
    
   

    public int getClassSessionId() {
        return classSessionId;
    }

    public void setClassSessionId(int classSessionId) {
        this.classSessionId = classSessionId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTeacherIpAddress() {
        return teacherIpAddress;
    }

    public void setTeacherIpAddress(String ipAddress) {
        this.teacherIpAddress = ipAddress;
    }

    public String getTeacherMulticastAddress() {
        return teacherMulticastAddress;
    }

    public void setTeacherMulticastAddress(String teacherMulticastAddress) {
        this.teacherMulticastAddress = teacherMulticastAddress;
    }

    public String getStudentMulticastAddress() {
        return studentMulticastAddress;
    }

    public void setStudentMulticastAddress(String studentMulticastAddress) {
        this.studentMulticastAddress = studentMulticastAddress;
    }

    @Override
    public String toString() {
        return classSessionId + ":" + courseId + ":" + courseTitle + ":" + section + ":"
                + teacherIpAddress + ":" + teacherMulticastAddress + ":" + studentMulticastAddress;
    }
}
