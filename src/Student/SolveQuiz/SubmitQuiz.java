/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student.SolveQuiz;

import Student.ConnectToDB;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Hamid
 */
public class SubmitQuiz {

    Quiz quizObj;

    SubmitQuiz() {
    }

    SubmitQuiz(Quiz quizObj) {
        this.quizObj = quizObj;
    }

    public void uploadQuizInDb(String studentId) throws SQLException, IOException {

        for (Answer answerObj : quizObj.getQuestionsArrayList()) {

            String sql = "INSERT INTO Answer (QuizQuestion_Id,Student_Id,Answer,Answer_ImageCoordinates) "
                    + "VALUES (?,?,?,?)";

            PreparedStatement statement = ConnectToDB.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, answerObj.getQuestionId());
            statement.setString(2, studentId);
            if (!answerObj.getStudentAnswer().trim().isEmpty()) {
                statement.setString(3, answerObj.getStudentAnswer());
            } else {
                statement.setString(3, "-1");
            }
            if (answerObj.getBoard()) {
                //file not exist error
                if (!readCoordinatesFromFile("Temp//" + answerObj.getQuestionNo() + ".text").trim().isEmpty()) {
                    statement.setString(4, readCoordinatesFromFile("Temp//" + answerObj.getQuestionNo() + ".text"));
                } else {
                    statement.setString(4, "-1");
                }
            } else {
                statement.setString(4, "-1");
            }

            int rowsInserted = statement.executeUpdate();
            int last_inserted_id = -1;
            if (rowsInserted > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    last_inserted_id = rs.getInt(1);
                }
                System.out.println("Answer Inserted:" + last_inserted_id);
            }
        }
        quizObj.getQuestionsArrayList().clear();
    }

    public static String readCoordinatesFromFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            return sb.toString();
        } finally {
            br.close();
        }
    }
}
