/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Skrzypek
 */
public class MOUtil {

    private MOUtil() {
    }

    public static String stringDateOfYear(String year) {
        return year + "-01-01";
    }

    public static Date dateOfYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.JANUARY, 1, 0, 0, 0);
        return cal.getTime();
    }
}
