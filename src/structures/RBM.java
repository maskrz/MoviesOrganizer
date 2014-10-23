/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import Jama.Matrix;
import entity.Movie;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrices.operations.CalculatedMatrixFactory;
import static matrices.operations.MatrixOperation.ADVERSE;
import static matrices.operations.MatrixOperation.CUMSUM;
import static matrices.operations.MatrixOperation.EXP;
import static matrices.operations.MatrixOperation.INCREMENT;
import static matrices.operations.MatrixOperation.INVERSE;
import static matrices.operations.MatrixOperation.NORMALIZE;
import static matrices.operations.MatrixOperation.ZEROS;
import moviesorganizer.OrganizerFacade;
import services.MoviesService;
import servicesImpl.MoviesServiceImpl;

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
    private double acc;

    private CalculatedMatrixFactory cmf;
    private Matrix h1;
    private Matrix v2;

    private MoviesService moviesService;

    public RBM() {
        questions = 20;
        concepts = 1008;
        features = 5204;
        hiddenUnits = 200;
        epochs = 50;
        minibatch = 10;
        alpha = 0.1;
        trainingSet = initializeTrainingSet();
        rbmTrain = new RBMtrain(getTrainingSet(), getTrainingSet().copy(), getHiddenUnits(), getEpochs(), getMinibatch(), getAlpha());
        this.cmf = new CalculatedMatrixFactory();
        this.moviesService = new MoviesServiceImpl();
    }

    public void train() {
        getRbmTrain().trainRBM();
        this.setWeights(getRbmTrain().getWeights());
        this.setProbabilities(getRbmTrain().getProbabilities());
        this.setHiddenMatrix(getRbmTrain().getHidden());
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
        for (int i = 0; i < getConcepts(); i++) {
            setxMatrix(new Matrix(generateXMatrix(i)));
//            cmf.printMatrix(trainingSet);
            getCmf().printMatrix(getxMatrix());
            setvMatrix(getCmf().singleMatrixOperation(new Matrix(getFeatures(), 1), ZEROS));
            getCmf().printMatrix(getvMatrix());
            double[] ids = generateIDs();
            for (int j = 0; j < getQuestions(); j++) {
                System.out.println("___________________________");
                setH1(calculateH1());
                setV2(calculateV2());
                setV2(selectV2(getV2(), ids));
                Matrix px = getCmf().singleMatrixOperation(getV2(), NORMALIZE);
                double r = Math.random();
                Matrix cumsum = getCmf().singleMatrixOperation(px, CUMSUM);
                Matrix l = lessThan(cumsum, r);
                int id = (int) sum(l);
                ids[j] = id;
                getvMatrix().set(id, 0, getxMatrix().get(0, id));
            }
            Matrix sim = getCmf().singleMatrixOperation(new Matrix(1, getConcepts()), ZEROS);
            for (int j = 0; j < getConcepts(); j++) {
                Matrix vIds = generateVIds(getvMatrix(), ids);
                Matrix nX = generateNX(j, ids);
                double norm = vIds.transpose().minus(nX).norm2();
                sim.set(0, j, norm);
            }
            double min = minimumValue(sim);
            Matrix temp = generateTemp(sim, min);
            double sumC = sumCon(temp, i);
            System.out.println(sumC);
            double lC = lengthCon(temp);
            System.out.println(lC);
            setAcc(getAcc() + (sumC / lC));
            System.out.println(getAcc());
        }
    }

    public void recognizeMovie(int movieId) {
        int[] stat = new int[6000];

        FeaturesVector vector = null;
        Movie m = moviesService.getMovieById(movieId);
        setParameters();

        // prepare features for movie
        String featuresLine = "";
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("serialized/" + m.getFilmwebId() + ".ser"));
            vector = (FeaturesVector) ois.readObject();
            System.out.println("deserialized: " + vector.size());
            featuresLine = vector.toString();
        } catch (Exception ex) {
            Logger.getLogger(OrganizerFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        double[][] temp = new double[1][featuresLine.length()];
        for (int i = 0; i < featuresLine.length(); i++) {
            temp[0][i] = (double) featuresLine.charAt(i) - 48;
        }

        for (int i = 0; i < 100; i++) {
            setxMatrix(new Matrix(temp));

            setvMatrix(getCmf().singleMatrixOperation(new Matrix(getFeatures(), 1), ZEROS));
            for (int j = 0; j < getQuestions(); j++) {
                System.out.println("Question no " + j);
                setH1(calculateH1());
                setV2(calculateV2());
                Matrix px = getCmf().singleMatrixOperation(getV2(), NORMALIZE);
                double r = Math.random();
                Matrix cumsum = getCmf().singleMatrixOperation(px, CUMSUM);
                Matrix l = lessThan(cumsum, r);
                int id = (int) sum(l);
//                System.out.println("Question: " + vector.getFeatures().get(id).getQuestion());
//                System.out.println("Answer: " + getxMatrix().get(movieId - 1, id));
                getvMatrix().set(id, 0, getxMatrix().get(0, id));
                stat[id] ++;
            }
        }
        for (int i = 0; i < 100; i ++) {
            System.out.println("Question: " + i);
        }

    }

    private void setParameters() {
        this.setFeatures(getProbabilities().getRowDimension());
        this.setConcepts(getTrainingSet().getRowDimension());
        setAcc(0);
    }

    private double[][] generateXMatrix(int row) {
        double[][] result = new double[1][getFeatures()];
        for (int i = 0; i < getFeatures(); i++) {
            result[0][i] = getTrainingSet().get(row, i);
        }
        return result;
    }

    private Matrix calculateH1() {
//        getCmf().printMatrix(getvMatrix().transpose());
//        getCmf().printMatrix(getWeights());
        Matrix result = getCmf().multipleMatrixOperations(
                getHiddenMatrix().transpose().plus(getvMatrix().transpose().times(getWeights())),
                ADVERSE, EXP, INCREMENT, INVERSE)
                .transpose();
        return result;
    }

    private Matrix calculateV2() {
        Matrix result = getCmf().multipleMatrixOperations(
                getProbabilities().plus(getWeights().times(getH1())),
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
        double[] ids = new double[getFeatures()];
        for (int i = 0; i < getFeatures(); i++) {
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
        Matrix result = new Matrix(getQuestions(), 1);
        for (int i = 0; i < getQuestions(); i++) {
            result.set(i, 0, vMatrix.get((int) ids[i], 0));
        }
        return result;
    }

    private Matrix generateNX(int j, double[] ids) {
        Matrix result = new Matrix(1, getQuestions());
        for (int i = 0; i < getQuestions(); i++) {
            result.set(0, i, getTrainingSet().get(j, (int) ids[i]));
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
                if (matrix.get(i, j) > 0 && j == n) {
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
                if (matrix.get(i, j) > 0) {
                    result += 1;
                }
            }
        }
        return result;
    }

    Matrix getHidden() {
        return getHiddenMatrix();
    }

    Matrix getProbabilities() {
        return probabilities;
    }

    Matrix getWeights() {
        return weights;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getConcepts() {
        return concepts;
    }

    public void setConcepts(int concepts) {
        this.concepts = concepts;
    }

    public int getFeatures() {
        return features;
    }

    public void setFeatures(int features) {
        this.features = features;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public int getHiddenUnits() {
        return hiddenUnits;
    }

    public void setHiddenUnits(int hiddenUnits) {
        this.hiddenUnits = hiddenUnits;
    }

    public int getEpochs() {
        return epochs;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public int getMinibatch() {
        return minibatch;
    }

    public void setMinibatch(int minibatch) {
        this.minibatch = minibatch;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public RBMtrain getRbmTrain() {
        return rbmTrain;
    }

    public void setRbmTrain(RBMtrain rbmTrain) {
        this.rbmTrain = rbmTrain;
    }

    public Matrix getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(Matrix trainingSet) {
        this.trainingSet = trainingSet;
    }

    public void setWeights(Matrix weights) {
        this.weights = weights;
    }

    public void setProbabilities(Matrix probabilities) {
        this.probabilities = probabilities;
    }

    public Matrix getHiddenMatrix() {
        return hiddenMatrix;
    }

    public void setHiddenMatrix(Matrix hiddenMatrix) {
        this.hiddenMatrix = hiddenMatrix;
    }

    public Matrix getxMatrix() {
        return xMatrix;
    }

    public void setxMatrix(Matrix xMatrix) {
        this.xMatrix = xMatrix;
    }

    public Matrix getvMatrix() {
        return vMatrix;
    }

    public void setvMatrix(Matrix vMatrix) {
        this.vMatrix = vMatrix;
    }

    public double getAcc() {
        return acc;
    }

    public void setAcc(double acc) {
        this.acc = acc;
    }

    public CalculatedMatrixFactory getCmf() {
        return cmf;
    }

    public void setCmf(CalculatedMatrixFactory cmf) {
        this.cmf = cmf;
    }

    public Matrix getH1() {
        return h1;
    }

    public void setH1(Matrix h1) {
        this.h1 = h1;
    }

    public Matrix getV2() {
        return v2;
    }

    public void setV2(Matrix v2) {
        this.v2 = v2;
    }

}
