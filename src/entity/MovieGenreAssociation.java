/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import javax.persistence.ManyToOne;

/**
 *
 * @author Skrzypek
 */
public class MovieGenreAssociation implements Serializable {
    
    private Movie movie;
    
    private Genre genre;

    @ManyToOne
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @ManyToOne
    public Genre getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public int hashCode() {
        return (movie.getId() + genre.getId() + "").hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovieGenreAssociation)) {
            return false;
        }
        MovieGenreAssociation other = (MovieGenreAssociation) object;
        if ((this.genre.getId() != other.genre.getId() ||
                this.movie.getId() != other.movie.getId())) {
            return false;
        }
        return true;
    }
}
