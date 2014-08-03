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
public class GenresExtractorTest {

    public GenresExtractorTest() {
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
        GenresExtractor instance = new GenresExtractor();
        instance.prepareData();
        
    }

    @Test
    public void testAll() {
        System.out.println("testAll");
        GenresExtractor instance = new GenresExtractor();
        instance.prepareData();
        ArrayList<Feature> features = (ArrayList<Feature>) instance.extractFeatures();
        Feature f = features.get(0);
        Movie m = (Movie) session.get(Movie.class, 1);
        Feature f2 = features.get(2);
        Assert.assertTrue(Feature.getValue(f, m));
        Assert.assertFalse(Feature.getValue(f2, m));
    }

}
