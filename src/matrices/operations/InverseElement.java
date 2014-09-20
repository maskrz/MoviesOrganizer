/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package matrices.operations;

/**
 *
 * @author Skrzypek
 */
public class InverseElement implements MatrixElementOperation {

    @Override
    public double performOperation(double element) {
        return 1/element;
    }

}
