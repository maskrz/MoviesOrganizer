/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures.extractors;

import entity.Movie;
import helpers.HibernateUtil;
import java.util.ArrayList;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import structures.Feature;

/**
 *
 * @author Skrzypek
 */
public class DoPExtractorTest {

    public DoPExtractorTest() {
    }

    private static Session session;

    @Before
    public void setUpOne() {
        //session.beginTransaction();
    }

    @After
    public void tearDownOnce() {
//        if(session.isOpen())
//            session.close();
    }

    @BeforeClass
    public static void setUpClass() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @AfterClass
    public static void tearDownClass() {
        if(session.isOpen())
            session.close();
    }

    @Test
    public void testPrepareData() {
        System.out.println("prepareData");
        DoPExtractor instance = new DoPExtractor();
        instance.prepareData();

    }

    @Test
    public void testAll() {
        System.out.println("testAll");
        DoPExtractor instance = new DoPExtractor();
        instance.prepareData();
        ArrayList<Feature> features = (ArrayList<Feature>) instance.extractFeatures();
        Feature f = features.get(0);
        Feature f1 = features.get(1);
        Feature f2 = features.get(2);
        Feature f3 = features.get(3);
        Feature f4 = features.get(4);
        Movie m = (Movie) session.get(Movie.class, 1);
        Assert.assertTrue(Feature.getValue(f, m));
        Assert.assertTrue(Feature.getValue(f1, m));
        Assert.assertTrue(Feature.getValue(f2, m));
        Assert.assertFalse(Feature.getValue(f3, m));
        Assert.assertFalse(Feature.getValue(f4, m));
    }

}
