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
public class RatingExtractorTest extends DbUnitTestCase {

    public RatingExtractorTest() {
        super("RatingExtractorTest");
    }

    @Test
    public void testPrepareData() {
        System.out.println("prepareData");
        RatingExtractor instance = new RatingExtractor();
        instance.prepareData();

    }

    @Test
    public void testAll() {
        System.out.println("testAll");
        RatingExtractor instance = new RatingExtractor();
        instance.prepareData();
        ArrayList<Feature> features = (ArrayList<Feature>) instance.extractFeatures();
        Feature f = features.get(0);
        Movie m = moviesService.getMovieById(2);
        Feature f1 = features.get(1);
        Feature f2 = features.get(2);
        Assert.assertFalse(Feature.getValue(f, m));
        Assert.assertTrue(Feature.getValue(f1, m));
        Assert.assertFalse(Feature.getValue(f2, m));
    }

}
