/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servicesImpl;

import entity.Genre;
import entity.Movie;
import entity.MovieGenre;
import helpers.ApplicationHelper;
import info.talacha.filmweb.models.Film;
import java.util.ArrayList;
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

}
