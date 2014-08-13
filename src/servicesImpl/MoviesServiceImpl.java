/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servicesImpl;

import entity.Movie;
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

}
