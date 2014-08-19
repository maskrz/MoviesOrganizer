/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processors.downloader;

import entity.Movie;
import helpers.MOUtil;
import info.talacha.filmweb.api.FilmwebApi;
import info.talacha.filmweb.models.Film;
import info.talacha.filmweb.models.Person;
import java.util.ArrayList;
import services.MoviesService;
import servicesImpl.MoviesServiceImpl;

/**
 *
 * @author Skrzypek
 */
public class PeopleDownloader {

    FilmwebApi fa;
    MoviesService moviesService;

    public PeopleDownloader() {
        fa = new FilmwebApi();
        moviesService = new MoviesServiceImpl();
    }

    public void downloadData() {
        ArrayList<Movie> movies = moviesService.getAllMovies();
        for(Movie m : movies) {
            extractPeople(m);
        }
    }

    private void extractPeople(Movie movie) {
        int year = MOUtil.getYearOfDate(movie.getPremiere());
        ArrayList<Film> films = fa.getFilmList(movie.getTitle(), year);
        for(Film film: films) {
            if(film.getTitle() != null || film.getPolishTitle() != null) {
                System.out.println("------------ Movie: " + movie.getTitle());
                ArrayList<Person> actors = film.getActors();
                ArrayList<Person> directors = film.getDirectors();
                ArrayList<Person> screenwriters = film.getScreenwriters();
                ArrayList<Person> music = film.getMusic();
                System.out.println("----- Savinn actors:");
                savePeople(movie, actors, "Aktor");
                System.out.println("----- Savinn directors:");
                savePeople(movie, directors, "Rezyser");
                System.out.println("----- Savinn screenwriters:");
                savePeople(movie, screenwriters, "Scenarzysta");
                System.out.println("----- Savinn music:");
                savePeople(movie, music, "Tworca muzyki");
//                for(String genreName : genres) {
//                    Genre genre = moviesService.getOrCreateGenreByName(genreName.trim());
//                    moviesService.addGenreToMovie(movie, genre);
//                    System.out.println("Genre added to movie: " + genre.getName() + " -> " + movie.getTitle());
//                }
//                System.out.println("---------- "+ movie.getTitle()+" ----------");
//                System.out.println(film.getGenre());
                return;
            }
        }
//        System.out.println(films.size());
    }

    private void savePeople(Movie movie, ArrayList<Person> people, String role) {
        for(Person person: people) {
            entity.Person p = moviesService.getOrCreatepersonByName(person.getName());
            moviesService.addPersonToMovie(movie, p, role);
            System.out.println(person.getName() + " " + person.getRole());
        }
    }
}
