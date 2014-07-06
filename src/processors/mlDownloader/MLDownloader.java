/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processors.mlDownloader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import processors.databaseCreator.DBCreator;
import utils.OrganizerUtils;

/**
 *
 * @author Skrzypek
 */
public class MLDownloader {

    private final int amount = 1000;
    private final String url = "http://www.filmweb.pl/search/film?q=&type=&startYear=&endYear=&countryIds=null&genreIds=null&startRate=&endRate=&startCount=&endCount=&sort=COUNT&sortAscending=false&c=portal&page=";
    private int pagesAmount;
    private StringBuilder moviesList;
    private String previous;
    private int index;
    int start;

    public MLDownloader() {
        pagesAmount = amount / 10;
        pagesAmount++; // increased because of numbering pages in loop from 1
        moviesList = new StringBuilder();
        previous = "";
        index = 1;
        start = 1;
    }

    public void downloadML() {
        try {
            for (int i = start; i < start + pagesAmount; i++) {
                URL filmweb = new URL(url + i);
                URLConnection un = filmweb.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        un.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String source = sb.toString();
                matchPattern(source);
            }
        } catch (Exception ex) {
            Logger.getLogger(MLDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void matchPattern(String source) {
        Pattern pattern = Pattern.compile("title=.{0,200}\\([0-9]{4}\\)\">");
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            handleMatch(matcher.group());
        }
        saveList();
    }

    private void handleMatch(String match) {
        int titleStart = 7;
        //int matchLength = match.length();
        int yearStart = match.indexOf("(") + 1;
        //System.out.println(yearStart + " ");
        String title = match.substring(titleStart, yearStart - 2);
        if (!title.equals(previous)) {
            System.out.println("Saving: " + index);
            previous = title;
            title = title.replace("&oacute;", "ó");
            title = title.replace("&amp;", "&");
            title = title.replace("&quot;", "\"");
            title = title.replace("&sup2;", "^");
            int year = Integer.valueOf(
                    match.substring(yearStart, yearStart + 4));
            moviesList.append(index).append(";").append(title)
                    .append(";").append(year).append(OrganizerUtils.newLine);
            index ++ ;
        }
    }

    private void saveList() {
        try {
            PrintWriter pw = new PrintWriter("moviesList.csv");
            pw.print(moviesList.toString());
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
