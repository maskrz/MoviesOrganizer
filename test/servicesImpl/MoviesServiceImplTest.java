/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servicesImpl;

import entity.Genre;
import entity.Movie;
import java.util.ArrayList;
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

    @Test
    public void testGetAllMovies() {
        System.out.println("getAllMovies");
        MoviesServiceImpl instance = new MoviesServiceImpl();
        ArrayList<Movie> result = instance.getAllMovies();
        assertEquals(2, result.size());
    }

    @Test
    public void testCreateMovie() {
        System.out.println("createMovie");
        MoviesServiceImpl instance = new MoviesServiceImpl();

//        Film film = new Film(null);
//        film.setCountries("Test");
//        film.setDuration(111);
//        film.setPolishTitle("pltest");
//        film.setRate(2f);
//        film.setVotes(333);
//        film.setYear(1111);


//        ArrayList<Movie> result = instance.getAllMovies();
//        assertEquals(2, result.size());
    }

    @Test
    public void testGetGenreByName() {
        System.out.println("getGenreByName");
        MoviesServiceImpl instance = new MoviesServiceImpl();
        Genre g = instance.getGenreByName("Familinjy");
        assertNotNull(g);
        assertEquals(2+"", g.getId()+"");
    }

}
