/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import Jama.Matrix;
import helpers.MOUtil;
import java.util.ArrayList;
import java.util.Collections;
import matrices.operations.CalculatedMatrixFactory;
import static matrices.operations.MatrixOperation.ADVERSE;
import static matrices.operations.MatrixOperation.COMPARE;
import static matrices.operations.MatrixOperation.EXP;
import static matrices.operations.MatrixOperation.INCREMENT;
import static matrices.operations.MatrixOperation.INVERSE;
import static matrices.operations.MatrixOperation.RANDOM;
import static matrices.operations.MatrixOperation.SUM_COLUMNS;

/**
 *
 * @author Skrzypek
 */
public class RBMtrain {

    // number of epochs
    private int epochs;
    // weights matrix
    private double[][] weights;
    // hidden units
    private double[] hidden;
    // number of hidden
    private int nH;
    // size of minibatch
    private int minibatchSize;
    // training rate
    private double trainingRate;
    // number of training concepts
    private final int tConcepts;
    // numbet of features
    private final int features;
    // training data
    private int[][] trainingSet;
    // start time, end Time
    Long startTime;
    Long endTime;
    double duration;
    int b;
    private double[][] ma;
    private double[][] mb;
    double alpha;
    Matrix mWeights;
    Matrix matrixA;
    Matrix matrixB;
    Matrix mXTemp;
    Matrix mi;
    Matrix h2;
    Matrix x2;
    Matrix mi2;

    private CalculatedMatrixFactory cmf;

    public RBMtrain() {
        cmf = new CalculatedMatrixFactory();
        features = 10;
        tConcepts = 10;
        nH = 100;
        weights = new double[tConcepts][nH];
        ranomWeights();
        mWeights = new Matrix(weights);
        hidden = new double[nH];
        randomHidden();
        ma = new double[features][1];
        randomA();
        matrixA = new Matrix(ma);
        mb = new double[nH][1];
        randomB();
        matrixB = new Matrix(mb);
        epochs = 1;
        minibatchSize = 5;
        b = (int) tConcepts / minibatchSize;
        trainingSet = initializeTrainingSet();
        alpha = 0.1;

    }

    private int[][] initializeTrainingSet() {
        int[][] result = {{1, 0, 1, 1, 0, 1, 0, 1, 1, 0},
        {0, 1, 0, 0, 1, 0, 0, 1, 1, 1},
        {0, 0, 0, 0, 1, 0, 1, 1, 0, 1},
        {1, 1, 0, 0, 0, 1, 0, 0, 1, 1},
        {0, 1, 0, 0, 1, 0, 1, 0, 1, 1},
        {0, 0, 0, 0, 0, 1, 0, 1, 1, 1},
        {1, 1, 1, 0, 1, 0, 0, 1, 1, 0},
        {0, 0, 0, 0, 1, 0, 1, 1, 0, 1},
        {1, 1, 0, 0, 0, 1, 0, 1, 1, 1},
        {0, 1, 0, 1, 1, 0, 0, 0, 1, 1}};
        return result;
    }

    private void ranomWeights() {
        for (int i = 0; i < tConcepts; i++) {
            for (int j = 0; j < features; j++) {
                // QUESTION - why 0.01 * gaussian?
                weights[i][j] = MOUtil.randomGaussian();
            }
        }
    }

    private void randomHidden() {
        for (int i = 0; i < nH; i++) {
            hidden[i] = MOUtil.randomGaussian();
        }
    }

    private void randomA() {
        for (int i = 0; i < features; i++) {
            ma[i][0] = MOUtil.randomGaussian();
        }
    }

    private void randomB() {
        for (int i = 0; i < nH; i++) {
            mb[i][0] = MOUtil.randomGaussian();
        }
    }

    public void trainRBM() {
        for (int i = 0; i < epochs; i++) {
            startTime = System.currentTimeMillis();
            trainEpoch();
            endTime = System.currentTimeMillis();
            duration = (endTime - startTime) / 1000;
            System.out.println(duration);
        }
    }

    private void trainEpoch() {
        for (int i = 0; i < minibatchSize; i++) {
            int[] batchID = new int[b * minibatchSize];
            fillBatch(batchID);
            calculateParameters(batchID);
        }
    }

    private void fillBatch(int[] batchID) {
        for (int i = 0; i < minibatchSize; i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int j = 0; j < b;) {
                temp.add(j++);
            }
            Collections.shuffle(temp);
            int c = 0;
            for (int j : temp) {
                batchID[b * i + c] = j;
                c++;
            }
        }
    }

    private void calculateParameters(int[] batchID) {
        for (int j = 0; j < b; j++) {
            calculateXTemp(batchID, j);
            calculateMI();
            calculateH2();
            calculateX2();
            calculateMI2();
            calculateW();
            calculateA();
            calculateB();
            calculateError();
        }
    }

    private void calculateXTemp(int[] batchID, int j) {
        mXTemp = new Matrix(generateXTemp(batchID, j));
    }

    private double[][] generateXTemp(int[] batchID, int index) {
        double[][] result = new double[minibatchSize][features];
        int counter = 0;
        for (int i = 0; i < features; i++) {
            if (batchID[i] == index) {
                for (int j = 0; j < features; j++) {
                    result[counter][j] = trainingSet[i][j];
                }
                counter++;
            }
        }
        return result;
    }

    private void calculateMI() {
        mi = cmf.multipleMatrixOperations(
                new Matrix(repmat(mb, minibatchSize)).plus(mXTemp.times(mWeights)),
                ADVERSE, EXP, INCREMENT, INVERSE);
    }

    private void calculateH2() {
        h2 = cmf.twoMatricesOperation(
                mi,
                cmf.singleMatrixOperation(mi, RANDOM),
                COMPARE);
    }

    private void calculateX2() {
        Matrix temp = cmf.multipleMatrixOperations(
                new Matrix(repmat(ma, minibatchSize)).plus(h2.times(mWeights.transpose())),
                ADVERSE, EXP, INCREMENT, INVERSE);
        x2 = cmf.twoMatricesOperation(
                temp,
                cmf.singleMatrixOperation(temp, RANDOM),
                COMPARE);
    }

    private void calculateMI2() {
        mi2 = cmf.multipleMatrixOperations(
                new Matrix(repmat(mb, minibatchSize)).plus(x2.times(mWeights)),
                ADVERSE, EXP, INCREMENT, INVERSE);

    }

    private void calculateW() {
        double par = alpha / minibatchSize;
        mWeights = mWeights.plus(
                (mXTemp.transpose().times(mi).minus(x2.transpose().times(mi2))).times(par));
    }

    private void calculateA() {
        double par = alpha / minibatchSize;
        matrixB = matrixB.plus(cmf.singleMatrixOperation(
                mXTemp.minus(x2).transpose().times(par),
                SUM_COLUMNS));
    }

    private void calculateB() {
        double par = alpha / minibatchSize;
        matrixB = matrixB.plus(cmf.singleMatrixOperation(
                mi.minus(mi2).transpose().times(par),
                SUM_COLUMNS));
    }

    private double[][] repmat(double[][] array, int times) {
        double[][] result = new double[times][array.length];
        for (int i = 0; i < times; i++) {
            for (int j = 0; j < array.length; j++) {
                result[i][j] = array[j][0];
            }
        }
        return result;
    }

    private void calculateError() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
