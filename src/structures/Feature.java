/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

import entity.Movie;
import helpers.HibernateUtil;
import java.io.Serializable;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Skrzypek
 */
public class Feature implements Serializable {
    private String question;
    private String query;
    private boolean value;
//    private static HashMap<String, String> parameters;

    public Feature(String question) {
        this.question = question;
        query = "";
        value = false;
//        parameters = new HashMap<>();
    }

//    public void addParameter(String key, String value) {
//        parameters.put(key, value);
//    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }


    public static boolean getValue(Feature feature, Movie movie) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery(feature.getQuery());
//        applyParameters(query);
        query.setParameter("movie_id", movie.getId());
        System.out.println(feature.getQuestion());
        boolean result = !query.list().isEmpty();
        session.close();
        return result;

    }

    @Override
    public boolean equals(Object other) {
        if(other != null) {
            Feature object = (Feature) other;
            if (object.getQuery().equals(this.getQuery())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return query.hashCode();
    }

    Object getStringValue() {
        return this.value ? "1" : "0";
    }
}
