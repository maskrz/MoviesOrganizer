/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import javax.persistence.ManyToOne;

/**
 *
 * @author Skrzypek
 */
public class MovieAwardAssociation {

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Award award;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    @Override
    public int hashCode() {
        return (movie.getId() + award.getId() + "").hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovieAwardAssociation)) {
            return false;
        }
        MovieAwardAssociation other = (MovieAwardAssociation) object;
        if ((this.award.getId() != other.award.getId() ||
                this.movie.getId() != other.movie.getId())) {
            return false;
        }
        return true;
    }

}
