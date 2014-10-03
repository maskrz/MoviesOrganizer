/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processors.helpers;

import entity.Movie;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.MoviesService;
import servicesImpl.MoviesServiceImpl;
import structures.Feature;
import structures.FeaturesVector;

/**
 *
 * @author Skrzypek
 */
public class VectorFiller {

    private final String serializedPath;
    MoviesService ms;
    public VectorFiller() {
        ms = new MoviesServiceImpl();
        serializedPath = "serialized/";

    }

    public FeaturesVector fillVector(FeaturesVector vector) {
        ArrayList<Movie> movies = ms.getAllMovies();
        Movie m = movies.get(0);
        for(Feature feature : vector.getFeatures()) {
            feature.setValue(Feature.getValue(feature, m));
        }
        return vector;

    }

    public void fillAllMoviesVectors(FeaturesVector vector) {
        ArrayList<Movie> movies = ms.getAllMovies();
        int i = 1;
        int vSize = vector.getFeatures().size();
        for(Movie movie : movies) {
            System.out.println("Movie " + i + " of " + movies.size() + ", creating features vector");
            int j = 1;
            for(Feature feature : vector.getFeatures()) {
                System.out.println(j*100/vSize+ "%");
                feature.setValue(Feature.getValue(feature, movie));
                j++;
            }
            serializeAndSaveToDB(vector, movie);
            i++;
        }
    }

    private void serializeAndSaveToDB(FeaturesVector vector, Movie movie) {
        String serialized = serializeAndSave(vector, movie.getFilmwebId()+"");
        System.out.println("Serialized, save under path: " + serialized + ", saving to db");
        saveToDb(movie.getId(), serialized);
        System.out.println("Saved, success!");
    }

    private String serializeAndSave(FeaturesVector vector, String id) {
        try {
            ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(serializedPath+id+".ser"));
            ous.writeObject(vector);
            ous.close();
            return serializedPath + id;
                    } catch (IOException ex) {
            Logger.getLogger(VectorFiller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void saveToDb(Integer id, String serialized) {
        ms.addSerializedToMovie(id, serialized);
    }
}
