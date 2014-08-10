/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures.extractors;

import entity.Movie;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import servicesImpl.DbUnitTestCase;
import structures.Feature;

/**
 *
 * @author Skrzypek
 */
public class RolesExtractorTest extends DbUnitTestCase {

    public RolesExtractorTest() {
        super("RolesExtractorTest");
    }

    @Test
    public void testPrepareData() {
        System.out.println("prepareData");
        RolesExtractor instance = new RolesExtractor();
        instance.prepareData();
    }

    @Test
    public void testAll() {
        System.out.println("testAll");
        RolesExtractor instance = new RolesExtractor();
        instance.prepareData();
        ArrayList<Feature> features = (ArrayList<Feature>) instance.extractFeatures();
        Assert.assertEquals(4, features.size());
        Feature f = features.get(0);
        Feature f1 = features.get(1);
        Feature f2 = features.get(2);
        Feature f3 = features.get(3);
        Movie m = moviesService.getMovieById(1);
        Assert.assertTrue(Feature.getValue(f, m));
        Assert.assertFalse(Feature.getValue(f2, m));
        Assert.assertTrue(Feature.getValue(f1, m));
        Assert.assertFalse(Feature.getValue(f3, m));
    }

}
