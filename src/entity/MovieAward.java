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
@Table(name = "movie_award")
@AssociationOverrides({
                        @AssociationOverride(name = "pk.movie",
                            joinColumns = @JoinColumn(name = "MOVIE_ID")),
                        @AssociationOverride(name = "pk.award",
                            joinColumns = @JoinColumn(name = "AWARD_ID"))   })
public class MovieAward implements Serializable {

    private Integer id;
    private MovieAwardAssociation pk = new MovieAwardAssociation();
    private Integer year;
    private String type;
    private String category;
    private String awardName;
    
    @Transient
    public Movie getMovie() {
        return getPk().getMovie();
    }

    public void setMovie(Movie movie) {
        getPk().setMovie(movie);
    }

    @Transient
    public Award getAward() {
        return getPk().getAward();
    }

    public void setAward(Award award) {
        getPk().setAward(award);
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MovieAwardAssociation getPk() {
        return pk;
    }

    public void setPk(MovieAwardAssociation pk) {
        this.pk = pk;
    }

@Column(name = "year")
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

@Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

@Column(name = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

@Column(name = "award_name")
    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		MovieAward that = (MovieAward) o;

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
