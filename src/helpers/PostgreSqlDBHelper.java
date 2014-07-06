/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Skrzypek
 */
public class PostgreSqlDBHelper extends DBHelper {

    private String username = "postgres";
    private String password = "root";
    String url = "jdbc:mysql://localhost/";
    
    public PostgreSqlDBHelper() {
        sDriverName = "org.sqlite.JDBC";
        sJdbc = "jdbc:sqlite";
    }

    @Override
    public Connection getConnection(String dataBase) {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSqlDBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
