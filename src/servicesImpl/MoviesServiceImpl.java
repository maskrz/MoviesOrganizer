/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servicesImpl;

import entity.Award;
import entity.AwardMoviePerson;
import entity.Genre;
import entity.Movie;
import entity.MovieAward;
import entity.MovieGenre;
import entity.MoviePerson;
import entity.Person;
import helpers.ApplicationHelper;
import info.talacha.filmweb.models.Film;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Query;
import services.MoviesService;

/**
 *
 * @author Skrzypek
 */
public class MoviesServiceImpl extends ApplicationService implements MoviesService {

    @Override
    public Movie getMovieById(int id) {
        return (Movie) session.get(Movie.class, id);
    }

    @Override
    public ArrayList<Movie> getAllMovies() {
        Query query = session.createQuery("from Movie"); 
        return (ArrayList<Movie>) query.list();
    }

    @Override
    public ArrayList<Person> getAllPerson() {
        Query query = session.createQuery("from Person");
        return (ArrayList<Person>) query.list();
    }

    @Override
    public void createMovie(Film film) {
        Movie movie = ApplicationHelper.transformFilmToEntity(film);
        session.beginTransaction();
        session.save(movie);
        session.getTransaction().commit();
        session.flush();

    }

    @Override
    public Genre getGenreByName(String name) {
        Query query = session.createQuery("from Genre where name = :name");
        query.setParameter("name", name);
        return query.list().size() > 0? (Genre) query.list().get(0) : null;
    }

    @Override
    public Genre getOrCreateGenreByName(String name) {
        Genre genre = getGenreByName(name);
        if (genre != null) {
            return genre;
        } else {
            genre = new Genre();
            genre.setName(name);
            session.beginTransaction();
            session.save(genre);
            session.getTransaction().commit();
//            session.close();
            return genre;
        }
    }

    @Override
    public void addGenreToMovie(Movie movie, Genre genre) {
        MovieGenre mg = new MovieGenre();
        mg.setGenre(genre);
        mg.setMovie(movie);
        movie.getGenres().add(mg);
        session.beginTransaction();
        session.save(movie);
        session.getTransaction().commit();
    }

    @Override
    public Person getPersonByName(String name) {
        Query query = session.createQuery("from Person where first_name = :name");
        query.setParameter("name", name);
        return query.list().size() > 0? (Person) query.list().get(0) : null;
    }

    @Override
    public Person getOrCreatepersonByName(String name) {
        Person person = getPersonByName(name);
        if (person != null) {
            return person;
        } else {
            person = new Person();
            person.setFirstName(name);
            person.setLastName("");
            person.setBitrh(new Date());
            person.setHeight(0);
            session.beginTransaction();
            session.save(person);
            session.getTransaction().commit();
//            session.close();
            return person;
        }
    }

    @Override
    public void addPersonToMovie(Movie movie, Person person, String role) {
        MoviePerson mp = new MoviePerson();
        mp.setPerson(person);
        mp.setMovie(movie);
        mp.setRole(role);
        movie.getPeople().add(mp);
        session.beginTransaction();
        session.save(movie);
        session.getTransaction().commit();
    }

    @Override
    public Award getAwardByName(String name) {
        Query query = session.createQuery("from Award where name = :name");
        query.setParameter("name", name);
        return query.list().size() > 0? (Award) query.list().get(0) : null;
    }

    @Override
    public Award getOrCreateAwardByName(String name) {
        Award award = getAwardByName(name);
        if (award != null) {
            return award;
        } else {
            award = new Award();
            award.setName(name);
            session.beginTransaction();
            session.save(award);
            session.getTransaction().commit();
//            session.close();
            return award;
        }
    }

    @Override
    public void addAwardToMovie(Movie currentMovie, Award a, String category) {
        MovieAward ma = new MovieAward();
        ma.setAward(a);
        ma.setCategory(category);
        ma.setMovie(currentMovie);
        ma.setType("Nagroda");
        ma.setAwardName("");
        ma.setYear(0);
        currentMovie.getAwards().add(ma);
        session.beginTransaction();
        session.save(currentMovie);
        session.getTransaction().commit();
    }

    @Override
    public MoviePerson getMoviePersonByMovieAndPerson(int movieId, int personId) {
        Query query = session.createQuery("from MoviePerson where movie_id = :movieId and person_id = :personId");
        query.setParameter("movieId", movieId);
        query.setParameter("personId", personId);
        return query.list().size() > 0? (MoviePerson) query.list().get(0) : null;
    }

    @Override
    public void addAwardToPeople(Movie currentMovie, Person person, Award a, String category) {
        MoviePerson mp = getMoviePersonByMovieAndPerson(currentMovie.getId(), person.getId());
        AwardMoviePerson amp = new AwardMoviePerson();
        amp.setAward(a);
        amp.setAwardName("");
        amp.setCategory(category);
        amp.setMoviePerson(mp);
        amp.setType("Nagroda");
        amp.setYear(0);
        session.beginTransaction();
        session.save(mp);
        session.getTransaction().commit();
    }

    @Override
    public int countRoles(int personId) {
        Query query = session.createQuery("select count(*) from MoviePerson where person_id = :personId");
        query.setParameter("personId", personId);
        Long result = (Long) query.uniqueResult();
        return result.intValue();
    }

    @Override
    public Person getPersonById(int id) {
        return (Person) session.get(Person.class, id);
    }

    @Override
    public void addCounter(int personId, int counter) {
        Person person = getPersonById(personId);
        person.setCounter(counter);
        session.beginTransaction();
        session.flush();
        session.update(person);
        session.flush();
        session.getTransaction().commit();
        
    }

    @Override
    public ArrayList<Person> getMovieCast(int movieId) {
        Query query = session.createQuery("from MoviePerson where movie_id = :movieId and role = 'Aktor'");
        query.setParameter("movieId", movieId);
        ArrayList<MoviePerson> mpList = (ArrayList<MoviePerson>) query.list();
        ArrayList<Person> people = new ArrayList();
        for(MoviePerson mp : mpList) {
            people.add(mp.getPerson());
        }
        return people;
    }

    @Override
    public void deactivatePerson(int personId) {
        Person person = getPersonById(personId);
        person.setActive(Boolean.FALSE);
        session.beginTransaction();
        session.update(person);
        session.getTransaction().commit();
    }

    @Override
    public void activatePerson(int personId) {
        Person person = getPersonById(personId);
        person.setActive(Boolean.TRUE);
        session.beginTransaction();
        session.update(person);
        session.getTransaction().commit();
    }

    @Override
    public ArrayList<Person> getActiveActors(int movieId) {
        Query query = session.createQuery("select mp from MoviePerson mp join mp.person where movie_id = :movieId and role = 'Aktor' and active = true");
        query.setParameter("movieId", movieId);
        ArrayList<MoviePerson> mpList = (ArrayList<MoviePerson>) query.list();
        ArrayList<Person> people = new ArrayList();
        for(MoviePerson mp : mpList) {
            people.add(mp.getPerson());
        }
        return people;
    }

    @Override
    public void addSerializedToMovie(int movieId, String path) {
        Movie movie = (Movie)session.get(Movie.class, movieId);
        movie.setSerialized(path);
        session.beginTransaction();
        session.update(movie);
        session.getTransaction().commit();
    }

}
