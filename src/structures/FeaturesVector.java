/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

import java.util.ArrayList;

/**
 *
 * @author Skrzypek
 */
public class FeaturesVector {
    private ArrayList<Feature> features;

    public FeaturesVector() {
        features = new ArrayList();
    }

    public void addFeatures(ArrayList<Feature> features) {
        this.features.addAll(features);
    }

    public ArrayList<Feature> getFeatures() {
        return this.features;
    }

    public int size() {
        return features.size();
    }
}
