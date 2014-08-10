/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures.extractors;

import entity.Award;
import helpers.HibernateUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
public class PersonAwardExtractor implements FeatureExtractor {
    private String question1;
    private String question2;
    private String query;
    Properties properties;
    InputStream input;

    public PersonAwardExtractor() {
        question1 = "";
        question2 = "";
        query = "";
        properties = new Properties();
        input = null;
    }

    @Override
    public void prepareData() {
        try {
            input = new FileInputStream("src\\structures\\extractors\\config\\personAward.properties");

            properties.load(input);

            question1 = properties.getProperty("question1");
            question2 = properties.getProperty("question2");
            query = properties.getProperty("query");

        } catch (IOException ex) {
            Logger.getLogger(MovieAwardExtractor.class.getName()).log(Level.SEVERE, null, ex);
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
        Query query = session.createQuery("from Award");
        List<Award> awards = query.list();

        System.out.println(awards.size());
        ArrayList<Feature> features = new ArrayList<>();
        for(Award award : awards) {
            Feature feature1 = new Feature(question1 + " " + award.getName()+ "?");
            Feature feature2 = new Feature(question2 + " " + award.getName()+ "?");
            feature1.setQuery(this.query + " and award_id = " + award.getId() + " and type = 'Nagroda'");
            feature2.setQuery(this.query + " and award_id = " + award.getId() + " and type = 'Nominacja'");
            features.addAll(Arrays.asList(feature1, feature2));
        }
        return features;
    }
}
