/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processors.mlDownloader;

import helpers.URLConnectionHelper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

    private int amount = 1000;
    private final String url = "http://www.filmweb.pl/search/film?q=&type=&startYear=&endYear=&countryIds=null&genreIds=null&startRate=&endRate=&startCount=&endCount=&sort=COUNT&sortAscending=false&c=portal&page=";
    private int pagesAmount;
    private StringBuilder moviesList;
    private String previous;
    private int index;
    int startPage;
    private final String TITLE_PATTERN = "title=.{0,200}\\([0-9]{4}\\)\">";
    private final int TITLE_START = 7;

    public MLDownloader(int amount) {
        this.amount = amount;
        pagesAmount = amount / 10;
        pagesAmount++; // increased because of numbering pages in loop from 1
        moviesList = new StringBuilder();
        previous = "";
        index = 1;
        startPage = 1;
    }

    public void downloadML() {
        for (int i = startPage; i < startPage + pagesAmount; i++) {
            downloadFromPage(url + i);
        }
    }

    public void downloadFromPage(String pageUrl) {
        BufferedReader br = URLConnectionHelper.gerConnectionBR(pageUrl);
        String source = buildSource(br);
        matchPattern(source);
    }

    private String buildSource(BufferedReader br) {
        String source = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            source = sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(MLDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return source;
    }

    private void matchPattern(String source) {
        Pattern pattern = Pattern.compile(TITLE_PATTERN);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            handleMatch(matcher.group());
        }
        saveList();
    }

    private void handleMatch(String match) {
        int yearStart = match.indexOf("(") + 1;
        String title = match.substring(TITLE_START, yearStart - 2);
        if (!title.equals(previous)) {
            System.out.println("Saving: " + index);
            previous = title;
            title = replaceWebChars(title);
            int year = Integer.valueOf(
                    match.substring(yearStart, yearStart + 4));
            moviesList.append(index).append(";").append(title)
                    .append(";").append(year).append(OrganizerUtils.newLine);
            index++;
        }
    }

    private String replaceWebChars(String title) {
        title = title.replace("&oacute;", "ó");
        title = title.replace("&amp;", "&");
        title = title.replace("&quot;", "\"");
        title = title.replace("&sup2;", "^");
        title = title.replace("&middot;", "·");
        return title;
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
