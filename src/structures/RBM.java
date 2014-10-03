/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import Jama.Matrix;
import matrices.operations.CalculatedMatrixFactory;
import static matrices.operations.MatrixOperation.ADVERSE;
import static matrices.operations.MatrixOperation.CUMSUM;
import static matrices.operations.MatrixOperation.EXP;
import static matrices.operations.MatrixOperation.INCREMENT;
import static matrices.operations.MatrixOperation.INVERSE;
import static matrices.operations.MatrixOperation.NORMALIZE;
import static matrices.operations.MatrixOperation.ZEROS;

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

    private Matrix weights;
    private Matrix probabilities;
    private Matrix hiddenMatrix;
    private Matrix xMatrix;
    private Matrix vMatrix;
    double acc;

    private CalculatedMatrixFactory cmf;
    private Matrix h1;
    private Matrix v2;

    public RBM() {
        questions = 5;
        concepts = 8;
        features = 10;
        hiddenUnits = 100;
        epochs = 100;
        minibatch = 4;
        alpha = 0.1;
        trainingSet = initializeTrainingSet();
        rbmTrain = new RBMtrain(trainingSet, trainingSet.copy(), hiddenUnits, epochs, minibatch, alpha);
        this.cmf = new CalculatedMatrixFactory();
    }

    public void train() {
        rbmTrain.trainRBM();
        this.weights = rbmTrain.getWeights();
        this.probabilities = rbmTrain.getProbabilities();
        this.hiddenMatrix = rbmTrain.getHidden();
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

    public void execute() {
        setParameters();
        for (int i = 0; i < concepts; i++) {
            xMatrix = new Matrix(generateXMatrix(i));
//            cmf.printMatrix(trainingSet);
            cmf.printMatrix(xMatrix);
            vMatrix = cmf.singleMatrixOperation(new Matrix(features, 1), ZEROS);
            cmf.printMatrix(vMatrix);
            double[] ids = generateIDs();
            for (int j = 0; j < questions; j++) {
                System.out.println("___________________________");
                h1 = calculateH1();
//                cmf.printMatrix(h1);
                v2 = calculateV2();
//                cmf.printMatrix(v2);
//                System.out.println(Arrays.toString(ids));
                v2 = selectV2(v2, ids);
//                cmf.printMatrix(v2);
                Matrix px = cmf.singleMatrixOperation(v2, NORMALIZE);
//                System.out.println("px:");
//                cmf.printMatrix(px);
                double r = Math.random();
//                System.out.println("r: " + r);
                Matrix cumsum = cmf.singleMatrixOperation(px, CUMSUM);
//                System.out.println("cumsum:");
//                cmf.printMatrix(cumsum);
                Matrix l = lessThan(cumsum, r);
//                System.out.println("less: ");
//                cmf.printMatrix(l);
                int id = (int) sum(l);
//                System.out.println(id);
                ids[j] = id;
                vMatrix.set(id, 0, xMatrix.get(0, id));
//                cmf.printMatrix(vMatrix);
            }
            Matrix sim = cmf.singleMatrixOperation(new Matrix(1, concepts), ZEROS);
            for (int j = 0; j < concepts; j++) {
                Matrix vIds = generateVIds(vMatrix, ids);
//                cmf.printMatrix(vIds);
                Matrix nX = generateNX(j, ids);
//                cmf.printMatrix(trainingSet);
//                System.out.println(j);
//                System.out.println(Arrays.toString(ids));
//                cmf.printMatrix(nX);
//                cmf.printMatrix(vIds.transpose().minus(nX));
                double norm = vIds.transpose().minus(nX).norm2();
                sim.set(0, j, norm);
            }
//            cmf.printMatrix(sim);
            double min = minimumValue(sim);
            Matrix temp = generateTemp(sim, min);
//            cmf.printMatrix(temp);
            double sumC = sumCon(temp, i);
            System.out.println(sumC);
            double lC = lengthCon(temp);
            System.out.println(lC);
            acc += (sumC/lC);
            System.out.println(acc);
        }
    }

    private void setParameters() {
        this.features = probabilities.getRowDimension();
        this.concepts = trainingSet.getRowDimension();
        acc = 0;
    }

    private double[][] generateXMatrix(int row) {
        double[][] result = new double[1][features];
        for (int i = 0; i < features; i++) {
            result[0][i] = trainingSet.get(row, i);
        }
        return result;
    }

    private Matrix calculateH1() {
        Matrix result = cmf.multipleMatrixOperations(
                hiddenMatrix.transpose().plus(vMatrix.transpose().times(weights)),
                ADVERSE, EXP, INCREMENT, INVERSE)
                .transpose();
        return result;
    }

    private Matrix calculateV2() {
        Matrix result = cmf.multipleMatrixOperations(
                probabilities.plus(weights.times(h1)),
                ADVERSE, EXP, INCREMENT, INVERSE);
        return result;
    }

    private double sum(Matrix m) {
        double result = 0;
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                result += m.get(i, j);
            }
        }
        return result;
    }

    private Matrix lessThan(Matrix matrix, double parameter) {
        Matrix result = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                result.set(i, j, matrix.get(i, j) < parameter ? 1 : 0);
            }
        }
        return result;
    }

    private double[] generateIDs() {
        double[] ids = new double[features];
        for (int i = 0; i < features; i++) {
            ids[i] = -1;
        }
        return ids;
    }

    private Matrix selectV2(Matrix v2, double[] ids) {
        Matrix result = v2.copy();
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] != -1) {
                result.set((int) ids[i], 0, 0);
            }
        }
        return result;
    }

    private Matrix generateVIds(Matrix vMatrix, double[] ids) {
        Matrix result = new Matrix(questions, 1);
        for(int i = 0; i < questions; i ++) {
            result.set(i, 0, vMatrix.get((int)ids[i], 0));
        }
        return result;
    }

    private Matrix generateNX(int j, double[] ids) {
        Matrix result = new Matrix(1, questions);
        for(int i = 0; i < questions; i++) {
            result.set(0, i, trainingSet.get(j, (int)ids[i]));
        }
        return result;
    }

    private double minimumValue(Matrix matrix) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                min = matrix.get(i, j) < min ? matrix.get(i, j) : min;
            }
        }
        return min;
    }

    private Matrix generateTemp(Matrix matrix, double min) {
        Matrix result = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                result.set(i, j, matrix.get(i, j) == min ? 1 : 0);
            }
        }
        return result;
    }

    public double sumCon(Matrix matrix, int n) {
        double result = 0;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                if(matrix.get(i, j) > 0 && j == n) {
                    result += 1;
                }
            }
        }
        return result;
    }

    public double lengthCon(Matrix matrix) {
        double result = 0;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                if(matrix.get(i, j) > 0) {
                    result += 1;
                }
            }
        }
        return result;
    }

}
