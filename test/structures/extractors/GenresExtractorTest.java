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
public class GenresExtractorTest extends DbUnitTestCase {

    public GenresExtractorTest() {
        super("GenresExtractorTest");
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
        Movie m = moviesService.getMovieById(1);
        Feature f2 = features.get(2);
        Assert.assertTrue(Feature.getValue(f, m));
        Assert.assertFalse(Feature.getValue(f2, m));
    }

}
