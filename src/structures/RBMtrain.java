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

    public RBMtrain() {
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
        for(int i = 0; i < features; i++) {
            ma[i][0] = MOUtil.randomGaussian();
        }
    }

    private void randomB() {
        for(int i = 0; i < nH; i++) {
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
            //System.out.println(Arrays.toString(batchID));
            for (int j = 0; j < b; j++) {
                double[][] xTemp = generateXTemp(batchID, j);
                int d = features;

                // XTemp * W
                Matrix mXTemp = new Matrix(xTemp);
                //printMatrix(mXTemp);
                //printMatrix(mWeights);
                //System.out.println(mXTemp.getColumnDimension() + " " + mXTemp.getRowDimension());
                //System.out.println(mWeights.getColumnDimension() + " " + mWeights.getRowDimension());
                Matrix multipled = mXTemp.times(mWeights);
                //printMatrix(multipled);
                //System.out.println(multipled.getColumnDimension());
                //System.out.println(multipled.getRowDimension());

                //repmat(b',NTemp,1)
                double[][] repeated = repmat(mb, minibatchSize);
                Matrix r = new Matrix(repeated);
                //System.out.println(r.getColumnDimension());
                //System.out.println(r.getRowDimension());

                //bsxfun(@plus,repmat(b',NTemp,1),XTemp*W)
                Matrix bsxfun = r.plus(multipled);
                //printMatrix(r);
                //printMatrix(multipled);
                //printMatrix(bsxfun);

                // * -1
                Matrix inverse = bsxfun.times(-1);
                //printMatrix(inverse);

                //exp
                Matrix exp = expMatrix(inverse);
                //printMatrix(exp);

                // +1
                Matrix inc = incrementMatrix(exp);
                //printMatrix(inc);

                // 1/ matrix
                Matrix mi = divideByMatrix(inc);
                //printMatrix(mi);

                Matrix rand = randMatrix(mi);
                //printMatrix(rand);

                Matrix h2 = compareMatrices(mi, rand);
                //printMatrix(h2);

                // X2
                multipled = h2.times(mWeights.transpose());
                //printMatrix(mWeights);
                repeated = repmat(ma, minibatchSize);
                r = new Matrix(repeated);
                bsxfun = r.plus(multipled);
                inverse = bsxfun.times(-1);
                exp = expMatrix(inverse);
                inc = incrementMatrix(exp);
                Matrix temp = divideByMatrix(inc);
                rand = randMatrix(temp);
                Matrix x2 = compareMatrices(temp, rand);

                // MI2
                multipled = x2.times(mWeights);
                //printMatrix(mWeights);
                repeated = repmat(mb, minibatchSize);
                r = new Matrix(repeated);
                bsxfun = r.plus(multipled);
                inverse = bsxfun.times(-1);
                exp = expMatrix(inverse);
                inc = incrementMatrix(exp);
                temp = divideByMatrix(inc);
                rand = randMatrix(temp);
                Matrix mi2 = compareMatrices(temp, rand);
                //printMatrix(mi2);

                //W
                multipled = x2.transpose().times(mi2);
                temp = mXTemp.transpose().times(mi);
                bsxfun = temp.minus(multipled);
                double par = alpha/minibatchSize;
                temp = bsxfun.times(par);
                //printMatrix(mWeights);
                //printMatrix(temp);
                mWeights = mWeights.plus(temp);
                //printMatrix(mWeights);

                //a
                bsxfun = mXTemp.minus(x2);
                //printMatrix(bsxfun);
                Matrix sum = sumColumns(bsxfun);
                //printMatrix(sum);
                temp = sum.transpose();
                //printMatrix(temp);
                temp = temp.times(par);
                //printMatrix(temp);
                matrixA = matrixA.plus(temp);

                // b
                bsxfun = mi.minus(mi2);
                //printMatrix(bsxfun);
                sum = sumColumns(bsxfun);
                //printMatrix(sum);
                temp = sum.transpose();
                //printMatrix(temp);
                temp = temp.times(par);
                //printMatrix(temp);
                matrixB = matrixB.plus(temp);

                printMatrix(matrixA);
                printMatrix(matrixB);
                
            }
        }
    }

    private void fillBatch(int[] batchID) {
        for (int i = 0; i < minibatchSize; i++) {
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for (int j = 0; j < b;) {
                temp.add(j++);
            }
            Collections.shuffle(temp);
            int c = 0;
            for (int j : temp) {
                batchID[b * i + c] = j;
                c++;
            }
            c = 0;
        }
    }

    private double[][] generateXTemp(int[] batchID, int index) {
//        System.out.println(Arrays.toString(batchID));
        double[][] result = new double[minibatchSize][features];
        int counter = 0;
        for (int i = 0; i < features; i++) {
            if (batchID[i] == index) {
                //System.out.println("HIT");
                for (int j = 0; j < features; j++) {
                    result[counter][j] = trainingSet[i][j];
                }
                counter++;
            }
        }
        //System.out.println("----------------------------");
        return result;
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

    private void printMatrix(Matrix matrix) {
        System.out.println(matrix.getColumnDimension());
        System.out.println(matrix.getRowDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                System.out.print(matrix.get(i, j) + " ");
            }
            System.out.println("");
        }
    }

    private Matrix expMatrix(Matrix matrix) {
        Matrix result = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for(int i = 0; i < matrix.getRowDimension(); i ++) {
            for(int j = 0; j < matrix.getColumnDimension(); j ++) {
                result.set(i, j, Math.exp(matrix.get(i, j)));
            }
        }
        return result;
    }

    private Matrix incrementMatrix(Matrix matrix) {
        Matrix result = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for(int i = 0; i < matrix.getRowDimension(); i ++) {
            for(int j = 0; j < matrix.getColumnDimension(); j ++) {
                result.set(i, j, matrix.get(i, j) + 1);
            }
        }
        return result;
    }

    private Matrix divideByMatrix(Matrix matrix) {
        Matrix result = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for(int i = 0; i < matrix.getRowDimension(); i ++) {
            for(int j = 0; j < matrix.getColumnDimension(); j ++) {
                result.set(i, j, 1/matrix.get(i, j));
            }
        }
        return result;
    }

    private Matrix randMatrix(Matrix matrix) {
        Matrix result = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for(int i = 0; i < matrix.getRowDimension(); i ++) {
            for(int j = 0; j < matrix.getColumnDimension(); j ++) {
                result.set(i, j, Math.random());
            }
        }
        return result;
    }

    private Matrix compareMatrices(Matrix m1, Matrix m2) {
        Matrix result = new Matrix(m1.getRowDimension(), m1.getColumnDimension());
        for(int i = 0; i < m1.getRowDimension(); i ++) {
            for(int j = 0; j < m1.getColumnDimension(); j ++) {
                result.set(i, j, m1.get(i, j) > m2.get(i, j)? 1 : 0);
            }
        }
        return result;
    }

    private Matrix sumColumns(Matrix matrix) {
        Matrix result = new Matrix(1, matrix.getColumnDimension());
        for(int i = 0; i < matrix.getRowDimension(); i ++) {
            for(int j = 0; j < matrix.getColumnDimension(); j ++) {
                result.set(0, j, result.get(0, j) + matrix.get(i, j));
            }
        }
        return result;
    }

}
