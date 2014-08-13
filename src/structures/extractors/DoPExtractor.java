/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures.extractors;

import helpers.MOUtil;
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
public class DoPExtractor implements FeatureExtractor {
    private String question;
    private String query;
    Properties properties;
    InputStream input;
    private String[] dates;

    public DoPExtractor() {
        question = "";
        query = "";
        properties = new Properties();
        input = null;
        dates = null;
    }

    @Override
    public void prepareData() {
        try {
            input = new FileInputStream("src\\structures\\extractors\\config\\dop.properties");

            properties.load(input);

            question = properties.getProperty("question");
            query = properties.getProperty("query");
            dates = properties.getProperty("dates").split(";");

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
        for (String premiere : dates) {
            Feature feature = new Feature(question + " "+ premiere + " lub pozniej?");
            feature.setQuery(this.query + " and premiere > " + "'" + MOUtil.stringDateOfYear(premiere) + "'");
            features.add(feature);
        }
        return features;
    }
}
