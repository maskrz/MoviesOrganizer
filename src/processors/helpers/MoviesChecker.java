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
public class MoviesChecker {

    MoviesService ms;

    public MoviesChecker() {
        ms = new MoviesServiceImpl();
    }
    public void check() {
        ArrayList<Movie> movies = ms.getAllMovies();
        for(Movie movie : movies) {
            ArrayList<Person> people = ms.getActiveActors(movie.getId());
            System.out.println(movie.getTitle());
            if(people.size() < 1) {
                System.out.println("------ Alert" + movie.getTitle());
            }
            System.out.println(people.size());
        }
    }

}
