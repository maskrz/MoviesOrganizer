/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package matrices.operations;

import entity.Movie;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import moviesorganizer.OrganizerFacade;
import services.MoviesService;
import servicesImpl.MoviesServiceImpl;
import structures.Feature;
import structures.FeaturesMatrix;
import structures.FeaturesVector;

/**
 *
 * @author Skrzypek
 */
public class FeaturesMatrixCreator {


    public FeaturesMatrix createMatrix() {
        MoviesService ms = new MoviesServiceImpl();
        ArrayList<Movie> movies = ms.getAllMovies();
        //TODO
        int[][] result = new int[movies.size()][];
        System.out.println(movies.size());
        int i = 0;
        for(Movie movie : movies) {
            System.out.println(movie.getTitle());
            int[] array = deserializeMovie(movie.getSerialized());
            result[i++] = array;
//            System.out.println(Arrays.toString(result[i]));
//            printTab(result, i);
            System.out.println(i);
        }
        return new FeaturesMatrix(result);
    }

    private int[] deserializeMovie(String serialized) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serialized + ".ser"));
            FeaturesVector vector = (FeaturesVector) ois.readObject();
            System.out.println("deserialized: " + vector.size());
            int[] result = getFeaturesArray(vector);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(OrganizerFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private int[] getFeaturesArray(FeaturesVector vector) {
        int[] featuresArray = new int[vector.getFeatures().size()];
        int i = 0;
        for(Feature feature : vector.getFeatures()) {
            featuresArray[i++] = feature.getValue()? 1 : 0;
        }
        Arrays.toString(featuresArray);
        return featuresArray;
    }

    private void printTab(int[][] result, int k) {
        for(int i = 0; i < k; i ++) {
            String line = "";
            for(int j = 0; j < 5204; j++) {
                line += result[i][j] + " ";
            }
            System.out.println(line);
        }
    }
}
