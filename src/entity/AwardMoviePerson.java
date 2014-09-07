/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Skrzypek
 */
@Entity
@Table(name = "award_movie_person")
public class AwardMoviePerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "year")
    private Integer year;
    @Column(name = "type")
    private String type;
    @Column(name = "category")
    private String category;
    @Column(name = "award_name")
    private String awardName;

    @ManyToOne
    @JoinColumn (name = "movie_person_id")
    private MoviePerson moviePerson;

    @ManyToOne
    @JoinColumn (name = "award_id")
    private Award award;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public MoviePerson getMoviePerson() {
        return moviePerson;
    }

    public void setMoviePerson(MoviePerson moviePerson) {
        this.moviePerson = moviePerson;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }
}
