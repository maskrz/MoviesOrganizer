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
public class CompareElements implements TwoMatricesOperation {

    @Override
    public Matrix performOperation(Matrix matrix1, Matrix matrix2) {
        Matrix result = new Matrix(matrix1.getRowDimension(), matrix1.getColumnDimension());
        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix1.getColumnDimension(); j++) {
                result.set(i, j, matrix1.get(i, j) > matrix2.get(i, j) ? 1 : 0);
            }
        }
        return result;
    }

}
