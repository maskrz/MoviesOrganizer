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
public class CumSum implements SingleMatrixOperation {

    @Override
    public Matrix performOperation(Matrix matrix) {
        Matrix result = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
        double sum = 0;
        for(int i = 0; i < matrix.getRowDimension(); i ++) {
            sum += matrix.get(i, 0);
            result.set(i, 0, sum);
        }

        return result;
    }

}
