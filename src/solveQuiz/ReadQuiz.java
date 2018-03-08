/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveQuiz;

import Student.ConnectToDB;
import Student.Constants;
import Student.DownloadFile;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Hamid
 */
public class ReadQuiz {

    private Quiz quizObj;

    public ReadQuiz() {
        quizObj = new Quiz();
    }

    public Quiz getQuizObj() {
        return quizObj;
    }

    public void setQuizObj(Quiz quizObj) {
        this.quizObj = quizObj;
    }

    public void readQuizDetails(int quizId) throws SQLException {
        quizObj.setQuizId(quizId);

        String sql = "select q.Quiz_Number,q.Quiz_Time,q.Quiz_Taken,c.Course_Id,c.Course_Title from Quiz q,Course c where q.Course_Id=c.Course_Id and  Quiz_Id=" + quizObj.getQuizId();
        Statement statement = ConnectToDB.connection.createStatement();
        ResultSet result = statement.executeQuery(sql);

        while (result.next()) {
            quizObj.setQuizNo(result.getInt("Quiz_Number"));
//            quizObj.setQuizTime((10));
//            quizObj.setRemainingTime((10));
            quizObj.setQuizTime((result.getInt("Quiz_Time") * 60));
            quizObj.setRemainingTime((result.getInt("Quiz_Time") * 60));
            quizObj.setQuizTaken(result.getInt("Quiz_Taken"));
            quizObj.setCourseId("" + result.getInt("Course_Id"));
            quizObj.setCourseTitle(result.getString("Course_Title"));
        }
        System.out.println(quizObj.toString());

    }

    public void readQuestions() throws SQLException, IOException {

        quizObj.getQuestionsArrayList().clear();

        String sql = "select qqtn.QuizQuestion_Id,[Question],[Question_Marks] "
                + " ,[Question_ImageName],[Question_AnswerType],[Question_Options],[Question_Answer]"
                + ",[Question_MaximumWords],[Question_Board] from Question qtn,"
                + "QuizQuestion qqtn where qtn.Question_Id=qqtn.Question_Id and qqtn.Quiz_Id=" + quizObj.getQuizId();

        Statement statement = ConnectToDB.connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        int totalMarksTemp = 0, questionNo = 0;
        while (result.next()) {
            Answer questionObj = new Answer();
            questionObj.setQuestionNo(questionNo);
            questionNo++;
            questionObj.setQuestionId(result.getInt("QuizQuestion_Id"));
            questionObj.setQuestion(result.getString("Question"));
            questionObj.setMarks(result.getInt("Question_Marks"));
            totalMarksTemp += result.getInt("Question_Marks");
            if (!result.getString("Question_ImageName").isEmpty()
                    && !result.getString("Question_ImageName").trim().equalsIgnoreCase("-1")) {
                questionObj.setImage(true);
                questionObj.setImageUrl(result.getString("Question_ImageName").trim());
                //download image 
                String saveDir = "Temp//";
                String ServerUrl = "http://" + Constants.ServerIP + ":" + Constants.ServerPort + "/Files/QuestionImages/" + result.getString("Question_ImageName").trim();
                DownloadFile.downloadFile(ServerUrl, saveDir);
            } else {
                questionObj.setImage(false);
            }
            questionObj.setAnswerType(result.getString("Question_AnswerType"));
            if (result.getString("Question_AnswerType").equalsIgnoreCase("Mcqs")) {
                questionObj.removeAllOptions();
                questionObj.addOption(result.getString("Question_Options"));
                questionObj.setQuestionAnswer(result.getString("Question_Answer"));
            } else if (result.getString("Question_AnswerType").equalsIgnoreCase("OpenEnded")) {
                if (!result.getString("Question_Options").isEmpty()
                        && !result.getString("Question_Options").trim().equalsIgnoreCase("-1")) {
                    questionObj.removeAllOptions();
                    questionObj.addOption(result.getString("Question_Options"));
                }
                questionObj.setMaximumWords(result.getInt("Question_MaximumWords"));
                if (result.getInt("Question_Board") == 1) {
                    questionObj.setBoard(true);
                    createFileForBoard("Temp//" + questionObj.getQuestionNo());
                } else {
                    questionObj.setBoard(false);
                }
            }
            System.out.println(questionObj.toString());
            quizObj.setQuizTotalMarks(totalMarksTemp);
            quizObj.getQuestionsArrayList().add(questionObj);

        }

    }

    public void createFileForBoard(String fileName) {
        //creating file
        try {
            RandomAccessFile output = new RandomAccessFile(fileName + ".text", "rw");
            output.setLength(0);
            output.close();
        } catch (Exception eX) {
            System.out.println("code not working");
        }
    }

}
