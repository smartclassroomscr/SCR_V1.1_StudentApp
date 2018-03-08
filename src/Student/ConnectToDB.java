/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Hamid
 */
public class ConnectToDB {

    public static Connection connection = null;

    public static void connectToDB() {

        try {

            String dbURL = "jdbc:sqlserver://" + Constants.ServerDBIP + ":" + Constants.ServerDBPort + ";DatabaseName=SCRDB_log";
            String user = "sa";
            String pass = "hamid";
            connection = DriverManager.getConnection(dbURL, user, pass);
            System.out.println("DB Connected");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void discocnnectToDB() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
