/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import entity.Movie;
import info.talacha.filmweb.models.Film;
import java.math.BigDecimal;

/**
 *
 * @author Skrzypek
 */
public class ApplicationHelper {

    public static Movie transformFilmToEntity(Film film) {
        Movie movie = new Movie();
        movie.setBookTitle(null);
        movie.setDuration(film.getDuration());
        movie.setEvaluation(new BigDecimal(film.getRate()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        movie.setFilmwebId(film.getId());
        movie.setIsAdaptation(Boolean.FALSE);
        movie.setOriginalTitle(film.getTitle());
        movie.setPremiere(MOUtil.dateOfYear(film.getYear()));
        movie.setProduction(film.getCountries());
        movie.setTitle(film.getPolishTitle());
        return movie;
    }
}
