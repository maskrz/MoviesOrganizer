/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Skrzypek
 */
public class SQLiteDBHelper extends DBHelper {

    public SQLiteDBHelper() {
        sDriverName = "org.sqlite.JDBC";
        sJdbc = "jdbc:sqlite";
    }

    @Override
    public Connection getConnection(String dataBase) {
        try {
            Class.forName(sDriverName);
            String sDbUrl = sJdbc + ":" + dataBase;
            return DriverManager.getConnection(sDbUrl);
        } catch (Exception ex) {
            return null;
        }
    }

}
