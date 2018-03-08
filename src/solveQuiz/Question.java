/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveQuiz;

import java.util.ArrayList;

/**
 *
 * @author Hamid
 */
public class Question {
///questionId came from database
    //questionUpdated true if question change after reading from database

    private int questionId;
    private boolean questionUpdated;

    private int questionNo;
    private String question;
    private int marks;
    private boolean image;
    private String imageUrl;

    private String answerType;
//in case of mcqs this list hold the options
    //and in case of OpenEnded this list holds the keywords
    private ArrayList<String> options = new ArrayList<String>();
    private String questionAnswer;

    private int maximumWords;
    private boolean board;
    
    private int questionTime;

    Question() {
        questionId = -1;
        questionUpdated = false;
        image = false;
//        options.add("-1");
        questionAnswer = "";
        maximumWords = 0;
        board = false;
         questionTime=0;

    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public boolean getQuestionUpdated() {
        return questionUpdated;
    }

    public void setQuestionUpdated(boolean questionUpdated) {
        this.questionUpdated = questionUpdated;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void addOption(String option) {
        options.add(option);
    }

    public String getOption(int index) {
        return options.get(index);
    }

    public void removeAllOptions() {
        options.clear();
    }

    public String getAllOptions() {
        String temp = "";
        for (int i = 0; i < options.size(); i++) {
            temp += options.get(i) + ",";
        }
        if (!temp.isEmpty()) {
            temp = temp.substring(0, (temp.length() - 1));
        }
        return temp;
    }

    public void removeOption(int index) {
        options.remove(index);
    }

    public void setImage(boolean board) {
        this.image = board;
    }

    public boolean getImage() {
        return image;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setBoard(boolean board) {
        this.board = board;
    }

    public boolean getBoard() {
        return board;
    }

    public void setMaximumWords(int words) {
        this.maximumWords = words;
    }

    public int getMaximumWords() {
        return maximumWords;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public int getMarks() {
        return marks;
    }

    public int getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(int questionTime) {
        this.questionTime = questionTime;
    }
    public String toString() {
        return this.getQuestionId() + ":" + this.getQuestion() + ":" + this.getMarks() + ":" + this.getImage() + ":" + this.getImageUrl()
                + ":" + this.getAnswerType() + ":" + this.getAllOptions() + ":" + this.getQuestionAnswer()+ ":" + this.getMaximumWords() 
                + ":" + this.getBoard()+ ":" + this.getQuestionTime();
    }
}
