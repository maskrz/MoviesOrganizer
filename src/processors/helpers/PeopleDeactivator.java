/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processors.helpers;

import entity.Movie;
import entity.Person;
import java.util.ArrayList;
import services.MoviesService;
import servicesImpl.MoviesServiceImpl;

/**
 *
 * @author Skrzypek
 */
public class PeopleDeactivator {
    MoviesService ms;

    public PeopleDeactivator() {
        ms = new MoviesServiceImpl();
    }

    public void deactivatePeopleByMovie() {
        ArrayList<Movie> movies = ms.getAllMovies();
        for(Movie movie : movies) {
            checkCast(movie);
        }
    }

    private void checkCast(Movie movie) {
        ArrayList<Person> people = ms.getMovieCast(movie.getId());

    }

    public void deactivatePeopleByList() {
        ArrayList<Person> people = ms.getAllPerson();
        for(Person person : people) {
            deactivateIfNeeded(person);
        }
    }

    private void deactivateIfNeeded(Person person) {
        if(person.getCounter() < 3) {
            deactivate(person);
        } else {
            activate(person);
        }
    }

    private void deactivate(Person person) {
        ms.deactivatePerson(person.getId());
    }

    private void activate(Person person) {
        ms.activatePerson(person.getId());
    }
}
