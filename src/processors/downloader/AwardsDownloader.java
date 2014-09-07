/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processors.downloader;

import entity.Award;
import entity.Movie;
import entity.MoviePerson;
import entity.Person;
import helpers.MOUtil;
import helpers.URLConnectionHelper;
import info.talacha.filmweb.api.FilmwebApi;
import info.talacha.filmweb.models.Film;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import processors.mlDownloader.MLDownloader;
import services.MoviesService;
import servicesImpl.MoviesServiceImpl;

/**
 *
 * @author Skrzypek
 */
public class AwardsDownloader {
    FilmwebApi fa;
    MoviesService moviesService;
    String AWARD_PATTERN = "alt=Oscar>.*</span>";
    Movie currentMovie;

    public AwardsDownloader() {
        fa = new FilmwebApi();
        moviesService = new MoviesServiceImpl();
    }

    public void downloadData() {
        ArrayList<Movie> movies = moviesService.getAllMovies();
        for(Movie m : movies) {
            currentMovie = m;
            extractAwards(m);
        }
    }

    private void extractAwards(Movie movie) {
        int year = MOUtil.getYearOfDate(movie.getPremiere());
        ArrayList<Film> films = fa.getFilmList(movie.getTitle(), year);
        for(Film film: films) {
            if(film.getTitle() != null || film.getPolishTitle() != null) {
                String source = getSourceFromUrl(film.getFilmUrl()+"/awards");
                matchPattern(source);
                return;
            }
        }
    }

    private String getSourceFromUrl(String url) {
        System.out.println(url);
        BufferedReader br = URLConnectionHelper.gerConnectionBR(url);
        return buildSource(br);
    }

    private String buildSource(BufferedReader br) {
        String source = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            source = sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(MLDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return source;
    }
    
    private void matchPattern(String source) {
        int i = 0;
        String osc = "alt=Oscar";
        String span = "</span>";
        int is = 0;
        int ie = 0;
        i = is;
        boolean exists = true;
        while (exists) {
            is = source.indexOf(osc, i);
            ie = source.indexOf(span, is);
            if(is >= 0) {
                handleMatch(source.substring(is+15, ie));
            } else {
                exists = false;
            }
            i = ie;
        }
    }

    private void handleMatch(String match) {
        //System.out.println(match);
        //handleMovieAward(match);
        handleFullContent(match);
        /*
        if(match.contains("href") && !match.contains("Najlepszy film")) {
            handlePersonalAward(match);
        } else {
            handleMovieAward(match);
        }
                */
        
    }

    private void handlePersonalAward(String match) {
        String category = match.substring(match.indexOf("<span>") + 6, match.indexOf("<a href")).trim();
        //System.out.println(category);
        ArrayList<String> people = extractAwardedPeople(match, new ArrayList<String>());
        for(String name : people) {
            saveMoviePersonAward(name, replaceWebChars(category), "Oskar");
        }
        //System.out.println(people.size());
    }

    private void handleMovieAward(String match) {
        int lastIndex = match.indexOf("<a href") > 0? match.indexOf("<a href") : match.length();
        String category = match.substring(match.indexOf("<span>") + 6, lastIndex).trim();
        saveMovieAward(replaceWebChars(category), "Oskar");
        //System.out.println(category);

    }

    private ArrayList<String> extractAwardedPeople(String match, ArrayList<String> people) {
        //System.out.println(match);
        int end = match.lastIndexOf("</a>");
        if (end > 0) {
            String trimmed = match.substring(0, match.lastIndexOf("</a>")).trim();
            int last = trimmed.lastIndexOf(">")+1;
            String actor = trimmed.substring(last);
            people.add(actor);
            return extractAwardedPeople(trimmed.substring(0, trimmed.lastIndexOf(">")), people);
        } else {
            return people;
        }
        
    }

    private String replaceWebChars(String title) {
        title = title.replace("&oacute;", "ó");
        title = title.replace("&amp;", "&");
        title = title.replace("&quot;", "\"");
        title = title.replace("&sup2;", "^");
        title = title.replace("&middot;", "·");
        title = title.replace("&eacute;", "é");
        return title;
    }
    
    private void saveMovieAward(String category, String award) {
        System.out.println("Saving: "+ currentMovie.getTitle() + ", "+ award + ", "+category);
        Award a = moviesService.getOrCreateAwardByName(award);
        moviesService.addAwardToMovie(currentMovie, a, category);
        System.out.println("Award saved");
    }

    private void saveMoviePersonAward(String actor, String category, String award) {
        Award a = moviesService.getOrCreateAwardByName(award);
        Person person = moviesService.getPersonByName(actor);
        System.out.println("Saving: "+ currentMovie.getTitle() + ", "+ actor +", "+ award + ", "+category);
        if(person != null) {
            MoviePerson mp = moviesService.getMoviePersonByMovieAndPerson(currentMovie.getId(), person.getId());
            if(mp != null) {
                moviesService.addAwardToPeople(currentMovie, person, a, category);
                System.out.println("Award saved");
            } else {
                System.out.println("ERROR!!! - no Movie - Person association");
            }
        } else {
            System.out.println("ERROR!!! - person does not exist");
        }
    }

    private void handleFullContent(String match) {
        //System.out.println(match);
        //int lastIndex = match.indexOf("<a href") > 0? match.indexOf("<a href") : match.length();
        String category = removeHtmlTags(match);
        //System.out.println(category);
        saveMovieAward(replaceWebChars(category), "Oskar");
    }

    private String removeHtmlTags(String match) {
        int open = match.indexOf("<");
        if(open >= 0) {
            int end = match.indexOf(">");
            String before = match.substring(0, open);
            String after = match.substring(end+1);
            return removeHtmlTags((before + after).trim());
        }
        return match;
    }
}
