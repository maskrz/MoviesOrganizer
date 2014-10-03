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
public class NormalizeMatrix implements SingleMatrixOperation {

    @Override
    public Matrix performOperation(Matrix matrix) {
        double sum = 0;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j ++) {
                sum += matrix.get(i, j);
            }
        }
        Matrix result = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j ++) {
                result.set(i, j, matrix.get(i, j)/sum);
            }
        }
        return result;
    }

}
