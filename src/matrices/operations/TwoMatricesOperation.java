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
public interface TwoMatricesOperation {

    public Matrix performOperation(Matrix matrix1, Matrix matrix2);
}
