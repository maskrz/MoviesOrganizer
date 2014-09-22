/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package matrices.operations;

import Jama.Matrix;

/**
 *
 * @author Skrzypek
 */
public class CalculatedMatrixFactory {

    public Matrix singleMatrixOperation(Matrix matrix, MatrixOperation operation) {
        switch(operation) {
            case INCREMENT :
                return performMatrixElementsOperation(matrix, new IncrementElement());
            case EXP:
                return performMatrixElementsOperation(matrix, new ExpElement());
            case INVERSE:
                return performMatrixElementsOperation(matrix, new InverseElement());
            case RANDOM:
                return performMatrixElementsOperation(matrix, new RandomElement());
            case ADVERSE:
                return performMatrixElementsOperation(matrix, new AdverseElement());
            case SUM_COLUMNS:
                return performSingleMatrixOperation(matrix, new SumColumns());
            case SUM_ROWS:
                return performSingleMatrixOperation(matrix, new SumRows());
            case ONES:
                return performSingleMatrixOperation(matrix, new OnesMatrix());
            default :
                return null;
        }
    }

    private Matrix performMatrixElementsOperation(Matrix matrix, MatrixElementOperation operation) {
        Matrix result = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for(int i = 0; i < matrix.getRowDimension(); i ++) {
            for(int j = 0; j < matrix.getColumnDimension(); j ++) {
                result.set(i, j, operation.performOperation(matrix.get(i, j)));
            }
        }
        return result;
    }

    private Matrix performTwoMatricesOperation(Matrix matrix1, Matrix matrix2,
            TwoMatricesOperation operation) {
        return operation.performOperation(matrix1, matrix2);
    }

    private Matrix performSingleMatrixOperation(Matrix matrix, SingleMatrixOperation operation) {
        return operation.performOperation(matrix);
    }

    public Matrix multipleMatrixOperations(Matrix matrix, MatrixOperation ... operations) {
        for (MatrixOperation operation : operations) {
            matrix = singleMatrixOperation(matrix, operation);
        }
        return matrix;
    }

    public void printMatrix(Matrix matrix) {
        System.out.println(matrix.getRowDimension());
        System.out.println(matrix.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                System.out.print(matrix.get(i, j) + " ");
            }
            System.out.println("");
        }
    }

    public Matrix twoMatricesOperation(Matrix matrix1, Matrix matrix2, MatrixOperation operation) {
        switch (operation) {
            case COMPARE:
                return performTwoMatricesOperation(matrix1, matrix2, new CompareElements());
            case DIF_ABS:
                return performTwoMatricesOperation(matrix1, matrix2, new DifferenceAbs());
            default:
                return null;

        }
    }

    public Matrix repeatMatrix(Matrix matrix, int times) {
        double[][] result = new double[times][matrix.getColumnDimension()];
        for (int i = 0; i < times; i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                result[i][j] = matrix.get(0, j);
            }
        }
        return new Matrix(result);
    }
}
