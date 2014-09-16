/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

import org.junit.Test;

/**
 *
 * @author Skrzypek
 */
public class RBMtrainIT {

    public RBMtrainIT() {
    }

    @Test
    public void testTrainRBM() {
        System.out.println("trainRBM");
        RBMtrain instance = new RBMtrain();
        instance.trainRBM();
    }

}
