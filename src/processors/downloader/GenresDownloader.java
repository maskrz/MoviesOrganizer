/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processors.downloader;

import entity.Genre;
import entity.Movie;
import helpers.MOUtil;
import info.talacha.filmweb.api.FilmwebApi;
import info.talacha.filmweb.models.Film;
import java.util.ArrayList;
import services.MoviesService;
import servicesImpl.MoviesServiceImpl;

/**
 *
 * @author Skrzypek
 */
public class GenresDownloader {
    FilmwebApi fa;
    MoviesService moviesService;

    public GenresDownloader() {
        fa = new FilmwebApi();
        moviesService = new MoviesServiceImpl();
    }

    public void downloadData() {
        ArrayList<Movie> movies = moviesService.getAllMovies();
        for(Movie m : movies) {
            extractGenres(m);
        }
    }

    private void extractGenres(Movie movie) {
        int year = MOUtil.getYearOfDate(movie.getPremiere());
        ArrayList<Film> films = fa.getFilmList(movie.getTitle(), year);
        for(Film film: films) {
            if(film.getTitle() != null || film.getPolishTitle() != null) {
                String[] genres = film.getGenre().split(",");
                for(String genreName : genres) {
                    Genre genre = moviesService.getOrCreateGenreByName(genreName.trim());
                    moviesService.addGenreToMovie(movie, genre);
                    System.out.println("Genre added to movie: " + genre.getName() + " -> " + movie.getTitle());
                }
//                System.out.println("---------- "+ movie.getTitle()+" ----------");
//                System.out.println(film.getGenre());
                return;
            }
        }
//        System.out.println(films.size());
    }
}
