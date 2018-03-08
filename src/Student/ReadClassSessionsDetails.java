/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Hamid
 */
public class ReadClassSessionsDetails {

    ReadClassSessionsDetails() {

    }

    public ArrayList<ClassSessionDetails> readTeacherClassSessionsDetails() throws SQLException {
        ArrayList<ClassSessionDetails> classSessionArrayList = new ArrayList<ClassSessionDetails>();

        String sql = "select cs.ClassSession_Id,cs.Section,c.Course_Id,c.Course_Title,cs.Teacher_IpAddress,cs.Teacher_MulticastAddress,\n"
                + " cs.Student_MulticastAddress  from ClassSession cs,Course c,RegisterStudent rs where\n"
                + " cs.Course_Id=c.Course_Id and rs.ClassSession_Id= cs.ClassSession_Id and  cs.Teacher_IpAddress!='-1'"
                + " and rs.Status=1 and rs.Student_Id=" + Constants.StudentId;
        Statement statement = ConnectToDB.connection.createStatement();
        ResultSet result = statement.executeQuery(sql);

        while (result.next()) {
            ClassSessionDetails classSessionDetailsObj = new ClassSessionDetails();
            classSessionDetailsObj.setClassSessionId(result.getInt("ClassSession_Id"));
            classSessionDetailsObj.setCourseId(result.getString("Course_Id"));
            classSessionDetailsObj.setCourseTitle(result.getString("Course_Title"));
            classSessionDetailsObj.setSection(result.getString("Section"));
            classSessionDetailsObj.setTeacherIpAddress(result.getString("Teacher_IpAddress"));
            classSessionDetailsObj.setTeacherMulticastAddress(result.getString("Teacher_MulticastAddress"));
            classSessionDetailsObj.setStudentMulticastAddress(result.getString("Student_MulticastAddress"));

//            System.out.println(classSessionDetailsObj.toString());
            classSessionArrayList.add(classSessionDetailsObj);
        }
        return classSessionArrayList;
    }

}
