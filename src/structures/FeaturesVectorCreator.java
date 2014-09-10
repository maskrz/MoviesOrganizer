/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import structures.extractors.FeatureExtractor;

/**
 *
 * @author Skrzypek
 */
public class FeaturesVectorCreator {

    private Properties properties;
    private String[] extractors;

    public FeaturesVectorCreator() {
        try {
            properties = new Properties();
            InputStream input = new FileInputStream("src\\structures\\config\\vector.properties");
            properties.load(input);
            extractors = properties.getProperty("extractors").split(";");
        } catch (Exception ex) {
            Logger.getLogger(FeaturesVectorCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public FeaturesVector createFeaturesVector() {
        FeaturesVector features = new FeaturesVector();
        for(String extractor : extractors) {
            System.out.println("Extractig features: " + extractor);
            extractAndAdd(extractor, features);
            System.out.println("--------------- SIZE: " + features.size());
        }
        return features;
    }

    private void extractAndAdd(String extractor, FeaturesVector features) {
        try {
            String cl = "structures.extractors."+extractor;
            FeatureExtractor fe = (FeatureExtractor) Class.forName(cl).newInstance();
            System.out.println("Preparing data");
            fe.prepareData();
            System.out.println("Data prepared");
            features.addFeatures((ArrayList<Feature>) fe.extractFeatures());
        } catch (Exception ex) {
            Logger.getLogger(FeaturesVectorCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
