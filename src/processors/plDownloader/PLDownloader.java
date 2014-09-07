/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processors.plDownloader;

import helpers.URLConnectionHelper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import processors.databaseCreator.DBCreator;
import processors.mlDownloader.MLDownloader;
import utils.OrganizerUtils;

/**
 *
 * @author Skrzypek
 */
public class PLDownloader {

    private int amount = 1000;
    private final String url = "http://www.filmweb.pl/search/person?q=&sex=0&startBirthYear=&endBirthYear=&professionIds=null&startRate=&endRate=&startCount=&endCount=&sort=COUNT&sortAscending=false&c=portal&page=";
    private int pagesAmount;
    private StringBuilder peopleList;
    private String previous;
    private int index;
    int startPage;
    private final String PERSON_PATTERN = "hdr hdr-medium hitTitle\">";

    public PLDownloader(int amount) {
        this.amount = amount;
        pagesAmount = amount / 10;
        pagesAmount++; // increased because of numbering pages in loop from 1
        peopleList = new StringBuilder();
        previous = "";
        index = 1;
        startPage = 1;
    }

    public void downloadPL() {
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
        int startIndex = source.indexOf(PERSON_PATTERN);
        while (startIndex >= 0) {
            source = source.substring(startIndex + 26);
            int endIndex = source.indexOf("<");
            handleMatch(source, endIndex);
            startIndex = source.indexOf(PERSON_PATTERN);
        }
        saveList();
    }

    private void handleMatch(String source, int endIndex) {
        String person = source.substring(0, endIndex);
        person = person.trim();
        person = replaceWebChars(person);
        person = removeI(person);
        System.out.println("Saving: " + index + " - " + person);
        peopleList.append(index).append(";").append(person.trim()).append(OrganizerUtils.newLine);
        index++;

    }

    private String replaceWebChars(String title) {
        title = title.replace("&oacute;", "ó");
        title = title.replace("&amp;", "&");
        title = title.replace("&quot;", "\"");
        title = title.replace("&sup2;", "^");
        title = title.replace("&middot;", "·");
        title = title.replace("&egrave;", "è");
        title = title.replace("&eacute;", "é");
        title = title.replace("&Aring;", "Å");
        title = title.replace("&ocirc;", "ô");
        title = title.replace("&aacute;", "á");
        return title;
    }

    private String removeI(String person) {
        int iIndex = person.lastIndexOf("I");
        while (iIndex == person.length() - 1) {
            person = person.substring(0, iIndex).trim();
            iIndex = person.lastIndexOf("I");
        }
        return person;
    }

    private void saveList() {
        try {
            PrintWriter pw = new PrintWriter("peopleList.csv");
            pw.print(peopleList.toString());
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
