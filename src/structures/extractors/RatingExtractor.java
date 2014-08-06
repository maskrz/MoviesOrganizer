/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures.extractors;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import structures.Feature;

/**
 *
 * @author Skrzypek
 */
public class RatingExtractor implements FeatureExtractor {

    private String question;
    private String query;
    Properties properties;
    InputStream input;
    private String[] questions;
    private String[] bottom;
    private String[] top;

    public RatingExtractor() {
        question = "";
        query = "";
        properties = new Properties();
        input = null;
        questions = null;
        bottom = null;
        top = null;
    }

    @Override
    public void prepareData() {
        try {
            input = new FileInputStream("src\\structures\\extractors\\config\\rating.properties");

            properties.load(input);

            question = properties.getProperty("question");
            query = properties.getProperty("query");
            questions = properties.getProperty("questions").split(";");
            bottom = properties.getProperty("bottom").split(";");
            top = properties.getProperty("top").split(";");

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
        ArrayList<Feature> features = new ArrayList<>();
        int i = 0;
        for (String q : questions) {
            Feature feature = new Feature(question + " "+ q);
            feature.setQuery(this.query + " and evaluation >= " + bottom[i] + " and evaluation <= " + top[i]);
            features.add(feature);
            i++;
        }
        return features;
    }

}
