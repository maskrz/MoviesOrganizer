/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moviesorganizer;

import processors.databaseCreator.DBCreator;
import processors.mlDownloader.MLDownloader;

/**
 *
 * @author Skrzypek
 */
public class OrganizerFacade {

    public static void createDatabase(String dbName, boolean remove) {
        DBCreator dbc = new DBCreator(dbName, remove, "sqlite");
        dbc.createDatabase();
    }

    public static void downloadML(int moviesAmount) {
        MLDownloader mld = new MLDownloader(moviesAmount);
        mld.downloadML();
    }
}
