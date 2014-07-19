/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import entity.Genre;
import entity.Movie;
import entity.MovieGenre;
import java.util.Iterator;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Skrzypek
 */
public class HibernateUtilIT {

    public HibernateUtilIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getSessionFactory method, of class HibernateUtil.
     */
    @Test
    public void testMovieGenre() {
        System.out.println("TestMovieGenre");
        Session session = HibernateUtil.getSessionFactory().openSession();

	session.beginTransaction();
        Movie movie1 = (Movie) session.get(Movie.class, 2);
        Genre genre1 = (Genre) session.get(Genre.class, 1);
        Genre genre2 = (Genre) session.get(Genre.class, 2);
        session.getTransaction().commit();
        System.out.println(movie1.getGenres().size());
        Iterator it = movie1.getGenres().iterator();
        while(it.hasNext()) {
            MovieGenre mg = (MovieGenre)it.next();
            System.out.println(mg.getGenre().getName());
        }
        session.close();
    }

}
