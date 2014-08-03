/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures.extractors;

import java.util.List;
import structures.Feature;

/**
 *
 * @author Skrzypek
 */
public interface FeatureExtractor {

    public void prepareData();

    public List<Feature> extractFeatures();
}
