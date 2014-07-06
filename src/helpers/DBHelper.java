/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Skrzypek
 */
public class DBHelper {
    private final static String sDriverName = "org.sqlite.JDBC";
    private final static String sJdbc = "jdbc:sqlite";


    public static Connection getConnection(String dataBase) {
        try {
            Class.forName(sDriverName);
            String sDbUrl = sJdbc + ":" + dataBase;
            return DriverManager.getConnection(sDbUrl);
        } catch (Exception ex) {
            return null;
        }
    }

    public static void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean executeStatement(Connection conn, String statement) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(statement);
            stmt.close();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
