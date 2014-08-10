/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servicesImpl;

import entity.Movie;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Skrzypek
 */
public class MoviesServiceImplTest extends DbUnitTestCase {

    public MoviesServiceImplTest() {
        super("test");
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testGetMovieById() {
        System.out.println("getMovieById");
        int id = 1;
        MoviesServiceImpl instance = new MoviesServiceImpl();
        Movie result = instance.getMovieById(id);
        assertEquals("Tytanik", result.getTitle());
        assertEquals(2, result.getGenres().size());
    }

}
