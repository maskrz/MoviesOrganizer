/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processors.databaseCreator;

import helpers.DBHelper;
import java.io.File;
import java.sql.Connection;
import utils.OrganizerUtils;

/**
 *
 * @author Skrzypek
 */
public class DBCreator {

    private String dbName;
    private String dbFullName;
    private int tablesAmount;
    private boolean remove; // if database with given name arleady exists, removes it if true
    private StringBuilder dump;
    private FileHandler fHandler;
    private DBHelper dbHelper;

    public DBCreator(String fileName, boolean remove, String type) {
        dbHelper = DBHelper.getDBHelper(type);
        fHandler = new FileHandler(this, fileName);
        this.remove = remove;
        dump = new StringBuilder("");
    }

    public void createDatabase() {
        File db = fHandler.createIfPossible(dbFullName, remove);
        if (db != null) {
            System.out.println("creating " + dbName);
            createFromFile();
            fHandler.saveDump(dbName, dump);
            System.out.println("Database created");
        } else {
            System.out.println("DB arleady exists!");
        }
    }

    private void createFromFile() {
        System.out.println("Getting connection");
        Connection conn = dbHelper.getConnection(dbFullName);
        if (conn == null) {
            System.out.println("could not get connection!");
        } else {
            System.out.println("Connected");
            createTables(conn);
            dbHelper.closeConnection(conn);
        }
    }

    private void createTables(Connection conn) {
        for(int i = 0; i < tablesAmount; i++) {
            String statement = fHandler.prepareStatement();
            executeStatement(conn, statement);
        }
    }

    private void executeStatement(Connection conn, String statement) {
        System.out.println("Executing statement: " + statement);
        if (dbHelper.executeStatement(conn, statement)) {
            dump.append(statement).append(OrganizerUtils.newLine);
            System.out.println("Statement executed properly");
        } else {
            System.out.println("Something went wrong!");
        }
    }

    /**
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * @param dbFullName the dbFullName to set
     */
    public void setDbFullName(String dbFullName) {
        this.dbFullName = dbFullName;
    }

    /**
     * @param tablesAmount the tablesAmount to set
     */
    public void setTablesAmount(int tablesAmount) {
        this.tablesAmount = tablesAmount;
    }

}
