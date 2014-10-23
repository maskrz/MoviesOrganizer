/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

import Jama.Matrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.MoviesService;
import servicesImpl.MoviesServiceImpl;

/**
 *
 * @author Skrzypek
 */
public class MovieRecognizer {

    private int movieId;
    private MoviesService ms;
    private RBM rbm;

    public MovieRecognizer(int movieId) {
        this.movieId = movieId;
        ms = new MoviesServiceImpl();
        this.rbm = new RBM();
        rbm.setWeights(readMatrix("weights.txt", 5204, 200));
        rbm.setProbabilities(readMatrix("probabilities.txt", 5204, 1));
        rbm.setHiddenMatrix(readMatrix("hidden.txt", 200, 1));
        System.out.println("Matrices created");
    }

    public void recognizeMovie() {
        rbm.recognizeMovie(movieId);
    }

    private Matrix readMatrix(String matrixName, int rows, int columns) {
        double[][] temp = new double[rows][columns];
        try {
            File f = new File(matrixName);
            Scanner sc = new Scanner(f);
            for(int i = 0; i < rows; i++) {
                String line = sc.nextLine();
                String[] splitted = line.split(";");
                for(int j = 0; j < columns; j++) {
                    temp[i][j] = Double.valueOf(splitted[j].trim());
                }
            }
            return new Matrix(temp);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MovieRecognizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
