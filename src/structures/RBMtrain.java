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
import static matrices.operations.MatrixOperation.DIF_ABS;
import static matrices.operations.MatrixOperation.EXP;
import static matrices.operations.MatrixOperation.INCREMENT;
import static matrices.operations.MatrixOperation.INVERSE;
import static matrices.operations.MatrixOperation.ONES;
import static matrices.operations.MatrixOperation.RANDOM;
import static matrices.operations.MatrixOperation.SUM_COLUMNS;
import static matrices.operations.MatrixOperation.SUM_ROWS;

/**
 *
 * @author Skrzypek
 */
public class RBMtrain {

    // number of epochs
    private final int epochs;
    // weights matrix
    // hidden units
    // number of hidden
    private final int hiddenUnits;
    // size of minibatch
    private final int minibatchSize;
    // training rate
    private double trainingRate;
    // number of training concepts
    private final int concepts;
    // numbet of features
    private final int features;
    // training data
    private final Matrix trainingSet;
    // start time, end Time
    Long startTime;
    Long endTime;
    double duration;
    double alpha;
    Matrix weights;
//    Matrix matrixA;
//    Matrix matrixB;
    Matrix probabilities;
    Matrix mXTemp;
    Matrix mi;
    Matrix h2;
    Matrix x2;
    Matrix mi2;
    Matrix xVal;
    Matrix hiddenMatrix;
    int bParameter;

    private final CalculatedMatrixFactory cmf;

    public Matrix getProbabilities() {
        return this.probabilities;
    }

    public Matrix getHidden() {
        return this.hiddenMatrix;
    }

    public Matrix getWeights() {
        return this.weights;
    }

    public RBMtrain(Matrix trainingSet, Matrix xVal, int hiddenUnits,
            int epochs, int minibatch, double alpha) {
        cmf = new CalculatedMatrixFactory();
        this.trainingSet = trainingSet;
        this.xVal = xVal;
        this.hiddenUnits = hiddenUnits;
        this.epochs = epochs;
        this.minibatchSize = minibatch;
        this.alpha = alpha;
        this.concepts = trainingSet.getRowDimension();
        this.features = trainingSet.getColumnDimension();
        randomWeights();
        randomHidden();
        randomProbabilities();
        System.out.println("Constructed");
//        bParameter = (int) (this.concepts / this.minibatchSize);
        bParameter = 10;
    }

    private void randomWeights() {
        weights = new Matrix(features, hiddenUnits);
        for (int i = 0; i < features; i++) {
            for (int j = 0; j < hiddenUnits; j++) {
                weights.set(i, j, 0.01 * MOUtil.randomGaussian());
            }
        }
    }

    private void randomProbabilities() {
        probabilities = new Matrix(features, 1);
        for (int i = 0; i < features; i++) {
            probabilities.set(i, 0, 0.01 * MOUtil.randomGaussian());
        }
    }

    private void randomHidden() {
        hiddenMatrix = new Matrix(hiddenUnits, 1);
        for (int i = 0; i < hiddenUnits; i++) {
            hiddenMatrix.set(i, 0, 0.01 * MOUtil.randomGaussian());
        }
    }

    public void trainRBM() {
            startTime = System.currentTimeMillis();
        for (int i = 0; i < epochs; i++) {
            long startE = System.currentTimeMillis();
            System.out.println("Epoch: " + i + " of " + epochs);
            trainEpoch();
            long endE = System.currentTimeMillis();
            double d = (endE - startE) / 1000;
            System.out.println("Epoch time: " + d);
        }
            endTime = System.currentTimeMillis();
            duration = (endTime - startTime) / 1000;
            System.out.println(duration);
    }

    private void trainEpoch() {
        for (int i = 0; i < minibatchSize; i++) {
            long startE = System.currentTimeMillis();
            System.out.println("Batch: " + i + " of " + minibatchSize);
            int[] batchID = new int[bParameter * minibatchSize];
            fillBatch(batchID);
            System.out.println("Batch filled");
            calculateParameters(batchID);
            long endE = System.currentTimeMillis();
            double d = (endE - startE) / 1000;
            System.out.println("Batch time: " + d);
        }
    }

    private void fillBatch(int[] batchID) {
        for (int i = 0; i < minibatchSize; i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int j = 0; j < bParameter;) {
                temp.add(j++);
            }
            Collections.shuffle(temp);
            int c = 0;
            for (int j : temp) {
                batchID[bParameter * i + c] = j;
                c++;
            }
        }
    }

    private void calculateParameters(int[] batchID) {
        for (int j = 0; j < bParameter; j++) {
            long startE = System.currentTimeMillis();
            System.out.println("Calculating parameters: " + j + " of " + bParameter);
            calculateXTemp(batchID, j);
            System.out.println("Xtemp calculated");
            calculateMI();
            System.out.println("MI calculated");
            calculateH2();
            System.out.println("H2 calculated");
            calculateX2();
            System.out.println("X2 calculated");
            calculateMI2();
            System.out.println("MI2 calculated");
            calculateW();
            System.out.println("W calculated");
            calculateA();
            System.out.println("A calculated");
            calculateB();
            System.out.println("B calculated");
//            calculateError();
//            System.out.println("Error calculated");
            long endE = System.currentTimeMillis();
            double d = (endE - startE) / 1000;
            System.out.println("Time: " + d);
        }
    }

    private void calculateXTemp(int[] batchID, int j) {
        mXTemp = new Matrix(generateXTemp(batchID, j));
    }

    private double[][] generateXTemp(int[] batchID, int index) {
        //FIXME
        double[][] result = new double[minibatchSize][features];
        int counter = 0;
        for (int i = 0; i < minibatchSize; i++) {
            if (batchID[i] == index) {
                for (int j = 0; j < features; j++) {
                    result[counter][j] = trainingSet.get(i, j);
                }
                counter++;
            }
        }
        return result;
    }

    private void calculateMI() {
        mi = cmf.multipleMatrixOperations(
                cmf.repeatMatrix(hiddenMatrix.transpose(), minibatchSize)
                        .plus(mXTemp.times(weights)),
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
                cmf.repeatMatrix(probabilities.transpose(), minibatchSize).
                        plus(h2.times(weights.transpose())),
                ADVERSE, EXP, INCREMENT, INVERSE);
        x2 = cmf.twoMatricesOperation(
                temp,
                cmf.singleMatrixOperation(temp, RANDOM),
                COMPARE);
    }

    private void calculateMI2() {
        mi2 = cmf.multipleMatrixOperations(
                cmf.repeatMatrix(hiddenMatrix.transpose(), minibatchSize).
                        plus(x2.times(weights)),
                ADVERSE, EXP, INCREMENT, INVERSE);

    }

    private void calculateW() {
        double par = alpha / minibatchSize;
        weights = weights.plus(
                (mXTemp.transpose().times(mi).minus(x2.transpose().times(mi2))).times(par));
    }

    private void calculateA() {
        double par = alpha / minibatchSize;
        probabilities = probabilities.plus(cmf.singleMatrixOperation(
                mXTemp.minus(x2),
                SUM_COLUMNS)
                .transpose().times(par));
    }

    private void calculateB() {
        double par = alpha / minibatchSize;
        hiddenMatrix = hiddenMatrix.plus(cmf.singleMatrixOperation(
                mi.minus(mi2),
                SUM_COLUMNS)
                .transpose().times(par));

    }

    private void calculateError() {
        calculateMIOError();
        calculateX2Error();
        double error = errorValue();
        System.out.println("error: " + error);
    }

    private void calculateMIOError() {
        mi = cmf.multipleMatrixOperations(
                hiddenMatrix.times(cmf.singleMatrixOperation(new Matrix(1, concepts), ONES))
                .transpose()
                .plus(xVal.times(weights)),
                ADVERSE, EXP, INCREMENT, INVERSE);
    }

    private void calculateX2Error() {
        x2 = cmf.multipleMatrixOperations(
                probabilities.times(cmf.singleMatrixOperation(new Matrix(1, concepts), ONES))
                .transpose()
                .plus(mi.times(weights.transpose())),
                ADVERSE, EXP, INCREMENT, INVERSE);
    }

    private double errorValue() {
        double par = 1.0/(features * concepts);
        double error = cmf.multipleMatrixOperations(
                cmf.twoMatricesOperation(xVal, x2, DIF_ABS), SUM_COLUMNS, SUM_ROWS).get(0, 0);
        return par * error;
    }
}
