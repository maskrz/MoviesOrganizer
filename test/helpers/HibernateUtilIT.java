/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import entity.Award;
import entity.AwardMoviePerson;
import entity.Genre;
import entity.Movie;
import entity.MovieAward;
import entity.MovieGenre;
import entity.MoviePerson;
import entity.Person;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
        List <MovieAward> movies = movie.getAwards();
        Assert.assertEquals(1, movies.size());
        MovieAward association = movies.get(0);
        Assert.assertEquals("2000", association.getYear().toString());
        Assert.assertEquals("Najlepszy film", association.getCategory());
        Award award = association.getAward();
        Assert.assertEquals("Oskar", award.getName());

    }

    @Test
    @Ignore
    public void testAddAwardToMovie() {
        Movie movie = (Movie) session.get(Movie.class, 1);
        List <MovieAward> movies = movie.getAwards();
        Assert.assertEquals(1, movies.size());
        Award award = new Award();
        award.setName("Grammy");
        session.save(award);
        MovieAward ma = new MovieAward();
        ma.setAward(award);
        ma.setMovie(movie);
        ma.setCategory("Innowacyjny film");
        ma.setType("Nominacja");
        ma.setYear(2001);
        session.save(ma);
        session.getTransaction().commit();
        movie = (Movie) session.get(Movie.class, 1);
        movies = movie.getAwards();
        Assert.assertEquals(1, movies.size());
    }

    @Test
    public void testPerson() {
        Person person = (Person)session.get(Person.class, 1);
        Assert.assertEquals("Leonardo", person.getFirstName());
        Assert.assertEquals(2, person.getMovies().size());
    }

    @Test
    public void testAwardMoviePerson() {
        Award award = (Award) session.get(Award.class, 1);
        AwardMoviePerson mpa = award.getMoviesPerson().get(0);
        MoviePerson mp = mpa.getMoviePerson();
        Assert.assertEquals("Tytanik", mp.getMovie().getTitle());
        Assert.assertEquals("Leonardo", mp.getPerson().getFirstName());
    }

}
