/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import java.util.Date;
import org.junit.AfterClass;
import org.junit.Assert;
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
    public void testStringDateOfYear() {
        System.out.println("dateOfYear");
        String year = "1991";
        String expResult = "1991-01-01";
        String result = MOUtil.stringDateOfYear(year);
        assertTrue(expResult.equals(result));
    }

    @Test
    public void testDateOfYear() {
        Assert.assertEquals("Sat Jan 01 00:00:00 CET 2000", MOUtil.dateOfYear(2000).toString());
    }

    @Test
    public void testGetYearOfDate() {
        System.out.println("getYearOfDate");
        Date date = MOUtil.dateOfYear(2000);
        Assert.assertEquals(2000, MOUtil.getYearOfDate(date));
    }

    @Test
    public void test_randomGaussian() {
        System.out.println(MOUtil.randomGaussian());
        System.out.println(MOUtil.randomGaussian());
        System.out.println(MOUtil.randomGaussian());
        System.out.println(MOUtil.randomGaussian());
    }

}
