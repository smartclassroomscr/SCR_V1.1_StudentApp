/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveQuiz;

import Student.ConnectToDB;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Hamid
 */
public class Test {

    public static void main(String[] args) throws IOException, SQLException {
        ConnectToDB.connectToDB();
        ReadQuiz readQuizObj = new ReadQuiz();
        readQuizObj.readQuizDetails(15);
        readQuizObj.readQuestions();
        ShowQuizMain obj = new ShowQuizMain(readQuizObj.getQuizObj());
    }
}
