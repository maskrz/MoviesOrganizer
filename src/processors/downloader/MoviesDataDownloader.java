/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processors.downloader;

import info.talacha.filmweb.api.FilmwebApi;
import info.talacha.filmweb.models.Film;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.MoviesService;
import servicesImpl.MoviesServiceImpl;

/**
 *
 * @author Skrzypek
 */
public class MoviesDataDownloader {

    FilmwebApi fa;
    MoviesService moviesService;

    public MoviesDataDownloader() {
        fa = new FilmwebApi();
        moviesService = new MoviesServiceImpl();
    }
    
    public void downloadData() {
        try {
            Scanner sc = new Scanner(new File("moviesList.csv"));
            String line = "";
            while(sc.hasNext()) {
                line = sc.nextLine();
                extractInfo(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MoviesDataDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void extractInfo(String line) {
        String title = line.split(";")[1];
        int year = Integer.valueOf(line.split(";")[2]);
        ArrayList<Film> films = fa.getFilmList(title, year);
        for(Film film: films) {
            if(film.getTitle() != null || film.getPolishTitle() != null) {
                moviesService.createMovie(film);
                System.out.println(film.getTitle() + film.getCountries() + film.getDuration() + " created!");
                return;
            }
        }
//        System.out.println(films.size());
    }

}
