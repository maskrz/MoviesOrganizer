/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

import entity.Movie;
import helpers.HibernateUtil;
import java.util.HashMap;
import java.util.Map.Entry;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Skrzypek
 */
public class Feature {
    private String question;
    private String query;
    private static HashMap<String, String> parameters;

    public Feature(String question) {
        this.question = question;
        query = "";
        parameters = new HashMap<>();
    }

    public void addParameter(String key, String value) {
        parameters.put(key, value);
    }

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

    public static boolean getValue(Feature feature, Movie movie) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery(feature.getQuery());
        applyParameters(query);
        query.setParameter("movie_id", movie.getId());

        return !query.list().isEmpty();
    }

    private static void applyParameters(Query query) {
        for(Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }
}
