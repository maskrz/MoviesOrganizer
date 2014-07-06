/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Skrzypek
 */
public abstract class DBHelper {
    
    protected String sDriverName;
    protected String sJdbc;

    public abstract Connection getConnection(String dataBase);

    public void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean executeStatement(Connection conn, String statement) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(statement);
            stmt.close();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static DBHelper getDBHelper(String type) {
        switch (type) {
            case "sqlite" :
                return new SQLiteDBHelper();
            case "postgreSql" :
                return new PostgreSqlDBHelper();
            default :
                return null;
        }
    }
}
