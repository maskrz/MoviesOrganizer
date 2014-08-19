/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import entity.Genre;
import entity.Movie;
import entity.Person;
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

    Genre getGenreByName(String name);

    Genre getOrCreateGenreByName(String name);

    void addGenreToMovie(Movie movie, Genre genre);

    Person getPersonByName(String name);

    Person getOrCreatepersonByName(String name);

    void addPersonToMovie(Movie movie, Person person, String role);
}
