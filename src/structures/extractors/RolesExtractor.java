/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures.extractors;

import entity.MoviePerson;
import entity.Person;
import helpers.HibernateUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import structures.Feature;

/**
 *
 * @author Skrzypek
 */
public class RolesExtractor implements FeatureExtractor {

    private String question;
    private String query;
    Properties properties;
    InputStream input;

    public RolesExtractor() {
        question = "";
        query = "";
        properties = new Properties();
        input = null;
    }

    @Override
    public void prepareData() {
        try {
            input = new FileInputStream("src\\structures\\extractors\\config\\roles.properties");

            properties.load(input);

            question = properties.getProperty("question");
            query = properties.getProperty("query");

        } catch (IOException ex) {
            Logger.getLogger(GenresExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Feature> extractFeatures() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("select distinct mp from MoviePerson mp join mp.person p where p.active = true");
        List<MoviePerson> moviePeople = query.list();
        System.out.println(moviePeople.size());
        int i = 1;

        System.out.println(moviePeople.size());
        ArrayList<Feature> features = new ArrayList<>();
        HashSet<Feature> fts = new HashSet<>();
        for (MoviePerson moviePerson : moviePeople) {
            System.out.println(i++);
//            query = session.createQuery("from Movie where movie_id = "+ moviePerson.getMovie().getId());
//            Movie movie = (Movie) query.list().get(0);
            query = session.createQuery("from Person where id = " + moviePerson.getPerson().getId());
            Person person = (Person) query.list().get(0);
            Feature feature = new Feature(question.split(";")[0] + " " + person + " " + question.split(";")[1] + moviePerson.getRole() + "?");
            feature.setQuery(this.query + " and person_id = " + person.getId() + " and role = '" + moviePerson.getRole()+ "'");
            fts.add(feature);
            System.out.println(fts.size());
            //features.add(feature);
        }
        features.addAll(fts);
        return features;
    }

}
