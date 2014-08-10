/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Skrzypek
 */
public class FeaturesVectorCreatorTest {

    public FeaturesVectorCreatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testCreateFeaturesVector() {
        FeaturesVectorCreator fvc = new FeaturesVectorCreator();
        FeaturesVector features = fvc.createFeaturesVector();
        Assert.assertEquals(29, features.getFeatures().size());
    }

}
