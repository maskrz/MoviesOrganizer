/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Skrzypek
 */
@Entity
@Table(name="movie_genre")
@AssociationOverrides({
		@AssociationOverride(name = "pk.movie",
			joinColumns = @JoinColumn(name = "MOVIE_ID")),
		@AssociationOverride(name = "pk.genre",
			joinColumns = @JoinColumn(name = "GENRE_ID")) })
public class MovieGenre implements Serializable {

    private MovieGenreAssociation pk = new MovieGenreAssociation();
    private Integer id;

    @EmbeddedId
    public MovieGenreAssociation getPk() {
        return pk;
    }

    public void setPk(MovieGenreAssociation pk) {
        this.pk = pk;
    }



    @Transient
    public Movie getMovie() {
        return getPk().getMovie();
    }

    public void setMovie(Movie movie) {
        getPk().setMovie(movie);
    }

    @Transient
    public Genre getGenre() {
        return getPk().getGenre();
    }

    public void setGenre(Genre genre) {
        getPk().setGenre(genre);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		MovieGenre that = (MovieGenre) o;

		if (getPk() != null ? !getPk().equals(that.getPk())
				: that.getPk() != null)
			return false;

		return true;
	}

    @Override
	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}
}
