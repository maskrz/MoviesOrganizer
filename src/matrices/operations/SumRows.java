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
class SumRows implements SingleMatrixOperation {

    @Override
    public Matrix performOperation(Matrix matrix) {
        Matrix result = new Matrix(matrix.getRowDimension(), 1);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                result.set(i, 0, result.get(i, 0) + matrix.get(i, j));
            }
        }
        return result;
    }

}
