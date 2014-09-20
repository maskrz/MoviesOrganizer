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
                return performSingleMatrixOperation(matrix, new IncrementElement());
            case EXP:
                return performSingleMatrixOperation(matrix, new ExpElement());
            case INVERSE:
                return performSingleMatrixOperation(matrix, new InverseElement());
            case RANDOM:
                return performSingleMatrixOperation(matrix, new RandomElement());
            default :
                return null;
        }
    }

    private Matrix performSingleMatrixOperation(Matrix matrix, MatrixElementOperation operation) {
        Matrix result = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for(int i = 0; i < matrix.getRowDimension(); i ++) {
            for(int j = 0; j < matrix.getColumnDimension(); j ++) {
                result.set(i, j, operation.performOperation(matrix.get(i, j)));
            }
        }
        return result;
    }

    public Matrix multipleMatrixOperations(Matrix matrix, MatrixOperation ... operations) {
        for (MatrixOperation operation : operations) {
            matrix = singleMatrixOperation(matrix, operation);
        }
        return matrix;
    }
}
