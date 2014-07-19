/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import entity.Award;
import entity.Genre;
import entity.Movie;
import entity.MovieGenre;
import java.util.Iterator;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Skrzypek
 */
public class HibernateUtilIT {

    private static Session session;

    public HibernateUtilIT() {
    }

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

    /**
     * Test of getSessionFactory method, of class HibernateUtil.
     */
    @Test
    public void testMovieGenre() {
        System.out.println("TestMovieGenre");

	
        Movie movie1 = (Movie) session.get(Movie.class, 1);
        Genre genre1 = (Genre) session.get(Genre.class, 1);
        Genre genre2 = (Genre) session.get(Genre.class, 2);
        //System.out.println(movie1.getGenres().size());
        Iterator it = movie1.getGenres().iterator();
        while(it.hasNext()) {
            MovieGenre mg = (MovieGenre)it.next();
            System.out.println(mg.getGenre().getName());
        }
    }

    @Test
    public void testAward() {
        Award award = (Award)session.get(Award.class, 1);
        System.out.println(award.getName());
        Assert.assertEquals("Oskar", award.getName());
    }

    @Test
    public void testMovieAwardAssociation() {
        Movie movie = (Movie) session.get(Movie.class, 1);
        //Set<MovieAward> awards = movie.getAwards();
        //Assert.assertEquals("Oskar", awards.)
    }

}
