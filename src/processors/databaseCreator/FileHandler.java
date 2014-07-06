/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processors.databaseCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Skrzypek
 */
public class FileHandler {

    private DBCreator dbCreator;
    private String fileName;
    private File cFile; // configuration File
    Scanner sc;

    public FileHandler(DBCreator dbCreator, String fileName) {
        this.dbCreator = dbCreator;
        this.fileName = fileName;
        setConfFile(fileName);
        extractDBProperties();
        sc = null;
    }

    private void setConfFile(String dbName) {
        cFile = new File(dbName + ".txt");
    }

    private void extractDBProperties() {
        try {
            Scanner sc = new Scanner(cFile);
            String line = "";
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                if (line.startsWith("#DBN")) {
                    dbCreator.setDbName(line.split("=")[1]);
                    dbCreator.setDbFullName(line.split("=")[1] + ".sqlite");
                    dbCreator.setTablesAmount(Integer.valueOf(sc.nextLine()));
                    sc.close();
                    return;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public File createIfPossible(String dbFullName, boolean remove) {
        File dbFile = new File(dbFullName);
        if (dbFile.exists() && !remove) {
            return null;
        }
        if (dbFile.delete()) {
            return dbFile;
        } else {
            return dbFile;
        }
    }

    public String prepareStatement() {
        try {
            if (sc == null) {
                sc = new Scanner(cFile);
            }
            String line = "";
            while (sc.hasNext()) {
                line = sc.nextLine();
                System.out.println(line);
                if (line.startsWith("#TN")) {
                    String tableName = line.split("=")[1];
                    int columnAmount = Integer.valueOf(sc.nextLine());
                    System.out.println("Creating statement for table: " + tableName);
                    return createStatement(tableName, sc, columnAmount);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    private String createStatement(String tableName, Scanner sc, int columnAmount) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(tableName);
        sb.append(" (");
        for (int i = 0; i < columnAmount; i++) {
            sb.append(sc.nextLine());
            sb.append(" ");
        }
        sb.append(");");
        return sb.toString();
    }

    public void saveDump(String dumpName, StringBuilder dump) {
        try {
            PrintWriter pw = new PrintWriter(dumpName + ".dump");
            pw.print(dump.toString());
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
