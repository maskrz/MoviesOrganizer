/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Skrzypek
 */

@Entity
@Table(name="award")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Award.findAll", query = "SELECT a FROM Award a"),
    @NamedQuery(name = "Award.findById", query = "SELECT a FROM Award a WHERE a.id = :id"),
    @NamedQuery(name = "Award.findByName", query = "SELECT a FROM Award a WHERE a.name = :name")})
public class Award implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name="id", nullable=false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "award", cascade = CascadeType.ALL)
    private List<MovieAward> movies = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "award", cascade = CascadeType.ALL)
    private List<AwardMoviePerson> moviesPerson = new ArrayList<>();

    public Award() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public List<MovieAward> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieAward> movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Award other = (Award) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public List<AwardMoviePerson> getMoviesPerson() {
        return moviesPerson;
    }

    public void setMoviesPerson(List<AwardMoviePerson> moviesPerson) {
        this.moviesPerson = moviesPerson;
    }
}
