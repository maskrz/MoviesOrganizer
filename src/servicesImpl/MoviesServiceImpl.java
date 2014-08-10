/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servicesImpl;

import entity.Movie;
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

}
