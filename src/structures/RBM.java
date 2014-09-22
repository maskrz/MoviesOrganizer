/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

import Jama.Matrix;

/**
 *
 * @author Skrzypek
 */
public class RBM {
    // number of questions
    private int questions;
    // number of concepts (movies)
    private int concepts;
    // numbet of features
    private int features;
    //sprasity of correct answers
    private double q;
    //hidden units
    private int hiddenUnits;
    // epoches
    private int epochs;
    // size of minibatch
    private int minibatch;
    // alpha
    private double alpha;

    private RBMtrain rbmTrain;
    private Matrix trainingSet;


    public RBM() {
        questions = 20;
        concepts = 8;
        features = 10;
        hiddenUnits = 100;
        epochs = 100;
        minibatch = 4;
        alpha = 0.1;
        trainingSet = initializeTrainingSet();
        rbmTrain = new RBMtrain(trainingSet, trainingSet.copy(), hiddenUnits, epochs, minibatch, alpha);

    }

    public void train() {
        rbmTrain.trainRBM();
    }

    private Matrix initializeTrainingSet() {
        double[][] result = {{1, 0, 1, 1, 0, 1, 0, 1, 1, 0},
        {0, 1, 0, 0, 1, 0, 0, 1, 1, 1},
        {0, 0, 0, 0, 1, 0, 1, 1, 0, 1},
        {1, 1, 0, 0, 0, 1, 0, 0, 1, 1},
        {0, 1, 0, 0, 1, 0, 1, 0, 1, 1},
        {0, 0, 0, 0, 0, 1, 0, 1, 1, 1},
        {1, 1, 1, 0, 1, 0, 0, 1, 1, 0},
        {0, 0, 0, 0, 1, 0, 1, 1, 0, 1}};
        return new Matrix(result);
    }
}
