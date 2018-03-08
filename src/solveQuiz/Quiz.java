/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveQuiz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Hamid
 */
public class Quiz {

    private int quizId;
    private String courseId;
    private String courseTitle;
    private int quizNo;
    private int quizTotalMarks;
    private int quizTime;
    private int remainingTime;
    private int quizTaken;
    private String quizType;

    private int selected = -1;

    private ArrayList<Answer> questionsArrayList;

    Quiz() {
        questionsArrayList = new ArrayList<Answer>();
        selected = -1;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
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

    public int getQuizNo() {
        return quizNo;
    }

    public void setQuizNo(int quizNo) {
        this.quizNo = quizNo;
    }

    public int getQuizTotalMarks() {
        return quizTotalMarks;
    }

    public void setQuizTotalMarks(int quizTotalMarks) {
        this.quizTotalMarks = quizTotalMarks;
    }

    public int getQuizTime() {
        return quizTime;
    }

    public void setQuizTime(int quizTime) {
        this.quizTime = quizTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public int getQuizTaken() {
        return quizTaken;
    }

    public void setQuizTaken(int quizTaken) {
        this.quizTaken = quizTaken;
    }

    public ArrayList<Answer> getQuestionsArrayList() {
        return questionsArrayList;
    }

    public void setQuestionsArrayList(ArrayList<Answer> questionsArrayList) {
        this.questionsArrayList = questionsArrayList;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String convertSecToMinute(int second) {
        Date d = new Date(second * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    public String toString() {
        return quizId + ":" + selected + ":" + courseId + ":" + courseTitle + ":" + quizNo + ":" + quizTotalMarks + ":" + quizTime + ":" + quizTaken + ":" + quizType;
    }

}
