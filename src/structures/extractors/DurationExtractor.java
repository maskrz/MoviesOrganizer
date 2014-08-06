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
public class DurationExtractor implements FeatureExtractor {

    private String question;
    private String query;
    Properties properties;
    InputStream input;
    private String[] durations;

    public DurationExtractor() {
        question = "";
        query = "";
        properties = new Properties();
        input = null;
        durations = null;
    }

    @Override
    public void prepareData() {
        try {
            input = new FileInputStream("src\\structures\\extractors\\config\\duration.properties");

            properties.load(input);

            question = properties.getProperty("question");
            query = properties.getProperty("query");
            durations = properties.getProperty("durations").split(";");

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
        for (String duration : durations) {
            Feature feature = new Feature(question + " "+ duration + " min?");
            feature.setQuery(this.query + " and duration >= " + duration);
            features.add(feature);
        }
        return features;
    }
}
