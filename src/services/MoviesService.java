/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import entity.Award;
import entity.Genre;
import entity.Movie;
import entity.MoviePerson;
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

    ArrayList<Person> getAllPerson();

    void createMovie(Film film);

    Genre getGenreByName(String name);

    Genre getOrCreateGenreByName(String name);

    void addGenreToMovie(Movie movie, Genre genre);

    Person getPersonByName(String name);

    Person getOrCreatepersonByName(String name);

    void addPersonToMovie(Movie movie, Person person, String role);

    Award getAwardByName(String name);

    Award getOrCreateAwardByName(String name);

    public void addAwardToMovie(Movie currentMovie, Award a, String category);

    public void addAwardToPeople(Movie currentMovie, Person person, Award a, String category);

    MoviePerson getMoviePersonByMovieAndPerson(int movieId, int personId);

    int countRoles(int personId);
    
    Person getPersonById(int id);

    void addCounter(int personId, int counter);
}
