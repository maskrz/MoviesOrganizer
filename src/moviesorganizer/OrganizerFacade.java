/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moviesorganizer;

import processors.databaseCreator.DBCreator;
import processors.downloader.AwardsDownloader;
import processors.downloader.GenresDownloader;
import processors.downloader.MoviesDataDownloader;
import processors.downloader.PeopleDownloader;
import processors.missingMovies.MissingMoviesHandler;
import processors.mlDownloader.MLDownloader;
import processors.plDownloader.PLDownloader;

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

    public static void downloadMovies() {
        MoviesDataDownloader mdd = new MoviesDataDownloader();
        mdd.downloadData();
    }

    public static void missingMovies() {
        MissingMoviesHandler mmh = new MissingMoviesHandler();
        mmh.listMissingMovies();
    }

    public static void downloadGenres() {
        GenresDownloader gd = new GenresDownloader();
        gd.downloadData();
    }

    public static void downloadPeople() {
        PeopleDownloader pd = new PeopleDownloader();
        pd.downloadData();
    }

    public static void downloadAwards() {
        AwardsDownloader ad = new AwardsDownloader();
        ad.downloadData();
    }

    public static void downloadPL(int peopleAmount) {
        PLDownloader pl = new PLDownloader(peopleAmount);
        pl.downloadPL();
    }
    public static void countRoles() {
        
    }
}
