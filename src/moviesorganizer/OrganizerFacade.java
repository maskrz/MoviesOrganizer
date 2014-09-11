/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moviesorganizer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import processors.databaseCreator.DBCreator;
import processors.downloader.AwardsDownloader;
import processors.downloader.GenresDownloader;
import processors.downloader.MoviesDataDownloader;
import processors.downloader.PeopleDownloader;
import processors.helpers.MoviesChecker;
import processors.helpers.PeopleDeactivator;
import processors.helpers.RolesCounter;
import processors.helpers.VectorFiller;
import processors.missingMovies.MissingMoviesHandler;
import processors.mlDownloader.MLDownloader;
import processors.plDownloader.PLDownloader;
import structures.FeaturesVector;
import structures.FeaturesVectorCreator;
import structures.RBM;

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
        RolesCounter rc = new RolesCounter();
        rc.countRoles();
    }

    public static void deactivatePeople() {
        PeopleDeactivator pd = new PeopleDeactivator();
        pd.deactivatePeopleByList();
    }

    public static void createFeaturesVector() {
        FeaturesVectorCreator fvc = new FeaturesVectorCreator();
        FeaturesVector vector = fvc.createFeaturesVector();
        try {
            ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream("vector.ser"));
            ous.writeObject(vector);
            ous.close();
            System.out.println("Serialization completed");
        } catch (IOException ex) {
            Logger.getLogger(OrganizerFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void checkMovies() {
        MoviesChecker mc = new MoviesChecker();
        mc.check();
    }

    public static void deserialize() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("vector.ser"));
            FeaturesVector vector = (FeaturesVector) ois.readObject();
            System.out.println("deserialized: " + vector.size());
        } catch (Exception ex) {
            Logger.getLogger(OrganizerFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void fillVectors() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("vector.ser"));
            FeaturesVector vector = (FeaturesVector) ois.readObject();
            System.out.println("deserialized: " + vector.size());
            VectorFiller vf = new VectorFiller();
            vf.fillAllMoviesVectors(vector);
        } catch (Exception ex) {
            Logger.getLogger(OrganizerFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void testRBM() {
        RBM.test_rbm();

    }
}
