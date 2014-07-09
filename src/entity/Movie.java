/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Skrzypek
 */
@Entity
@Table(name = "movie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Movie.findAll", query = "SELECT m FROM Movie m"),
    @NamedQuery(name = "Movie.findById", query = "SELECT m FROM Movie m WHERE m.id = :id"),
    @NamedQuery(name = "Movie.findByFilmwebId", query = "SELECT m FROM Movie m WHERE m.filmwebId = :filmwebId"),
    @NamedQuery(name = "Movie.findByTitle", query = "SELECT m FROM Movie m WHERE m.title = :title"),
    @NamedQuery(name = "Movie.findByOriginalTitle", query = "SELECT m FROM Movie m WHERE m.originalTitle = :originalTitle"),
    @NamedQuery(name = "Movie.findByPremiere", query = "SELECT m FROM Movie m WHERE m.premiere = :premiere"),
    @NamedQuery(name = "Movie.findByDuration", query = "SELECT m FROM Movie m WHERE m.duration = :duration"),
    @NamedQuery(name = "Movie.findByEvaluation", query = "SELECT m FROM Movie m WHERE m.evaluation = :evaluation"),
    @NamedQuery(name = "Movie.findByProduction", query = "SELECT m FROM Movie m WHERE m.production = :production"),
    @NamedQuery(name = "Movie.findByIsAdaptation", query = "SELECT m FROM Movie m WHERE m.isAdaptation = :isAdaptation"),
    @NamedQuery(name = "Movie.findByBookTitle", query = "SELECT m FROM Movie m WHERE m.bookTitle = :bookTitle")})
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "filmweb_id", nullable = false)
    private int filmwebId;
    @Basic(optional = false)
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    @Basic(optional = false)
    @Column(name = "original_title", nullable = false, length = 255)
    private String originalTitle;
    @Basic(optional = false)
    @Column(name = "premiere", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date premiere;
    @Basic(optional = false)
    @Column(name = "duration", nullable = false)
    private int duration;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "evaluation", precision = 17, scale = 17)
    private Double evaluation;
    @Basic(optional = false)
    @Column(name = "production", nullable = false, length = 255)
    private String production;
    @Column(name = "is_adaptation")
    private Boolean isAdaptation;
    @Column(name = "book_title", length = 255)
    private String bookTitle;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "MOVIE_GENRE",
            joinColumns = {@JoinColumn(name = "MOVIE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "GENRE_ID")})
    private Set<Genre> genres = new HashSet<>();

    public Movie() {
    }

    public Movie(Integer id) {
        this.id = id;
    }

    public Movie(Integer id, int filmwebId, String title, String originalTitle, Date premiere, int duration, String production) {
        this.id = id;
        this.filmwebId = filmwebId;
        this.title = title;
        this.originalTitle = originalTitle;
        this.premiere = premiere;
        this.duration = duration;
        this.production = production;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFilmwebId() {
        return filmwebId;
    }

    public void setFilmwebId(int filmwebId) {
        this.filmwebId = filmwebId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Date getPremiere() {
        return premiere;
    }

    public void setPremiere(Date premiere) {
        this.premiere = premiere;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Double evaluation) {
        this.evaluation = evaluation;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public Boolean getIsAdaptation() {
        return isAdaptation;
    }

    public void setIsAdaptation(Boolean isAdaptation) {
        this.isAdaptation = isAdaptation;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movie)) {
            return false;
        }
        Movie other = (Movie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Movie[ id=" + id + " ]";
    }

}
