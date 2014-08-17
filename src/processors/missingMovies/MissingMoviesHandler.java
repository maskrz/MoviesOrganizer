/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processors.missingMovies;

import entity.Movie;
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
public class MissingMoviesHandler {
    private MoviesService moviesService;

    public MissingMoviesHandler() {
        moviesService = new MoviesServiceImpl();
    }

    public void listMissingMovies() {
        try {
            ArrayList<Movie> movies = moviesService.getAllMovies();
            Scanner sc = new Scanner(new File("moviesList.csv"));
            StringBuilder sb = new StringBuilder();
            String line = "";
            int i = 0;
            while(sc.hasNext()) {
                line = sc.nextLine();
                String title = line.split(";")[1];
                boolean found = false;
                for(Movie m : movies) {
                    if(m.getTitle().equals(title)) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    i++;
                    System.out.println(i);
                    sb.append(title);
                    sb.append("\n");
                }
            }
            System.out.println(i);
            System.out.println(sb.toString());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MissingMoviesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
