/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Skrzypek
 */
public class URLConnectionHelper {

    public static URLConnection getConnection(String url) {
        try {
            URL page = new URL(url);
            URLConnection un = page.openConnection();
            return un;
        } catch (Exception ex) {
            Logger.getLogger(URLConnectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static BufferedReader gerConnectionBR(String url) {
        try {
            URLConnection un = URLConnectionHelper.getConnection(url);
            return new BufferedReader(new InputStreamReader(
                    un.getInputStream(), "UTF-8"));
        } catch (Exception ex) {
            Logger.getLogger(URLConnectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
