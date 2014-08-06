/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Skrzypek
 */
public class MOUtilTest {

    public MOUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testDateOfYear() {
        System.out.println("dateOfYear");
        String year = "1991";
        String expResult = "1991-01-01";
        String result = MOUtil.dateOfYear(year);
        assertTrue(expResult.equals(result));
    }

}
