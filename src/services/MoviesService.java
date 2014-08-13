/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import entity.Movie;
import info.talacha.filmweb.models.Film;
import java.util.ArrayList;

/**
 *
 * @author Skrzypek
 */
public interface MoviesService {

    Movie getMovieById(int id);

    ArrayList<Movie> getAllMovies();

    void createMovie(Film film);

}
