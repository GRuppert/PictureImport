/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comparison;

import Main.PicOrganizes;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor
 */
public class StaticTools {
    /** Reads and compares two csv files and reports the result to standard output */
    private static void compareCSV() {
        CsvParserSettings csvParserSettings = new CsvParserSettings();
        CsvParser parser = new CsvParser(new CsvParserSettings());
        try {
            List<String[]> filterData = parser.parseAll(new FileReader("e:\\filter.csv"));
            List<String[]> backupData = parser.parseAll(new FileReader("e:\\fekete.csv"));
            ArrayList<String[]> sizeMismatch = new ArrayList();
            ArrayList<String[]> wrong = new ArrayList();
            data:
            for (String[] data : backupData) {
                for (String[] filter : filterData) {
                    if (filter[0].endsWith(data[0])) {
                        if (filter[1].equals(data[1])) {
                            break data;
                        } else {
                            sizeMismatch.add(data);
                            break data;
                        }
                    }
                }
                wrong.add(data);
            }
            System.out.println("Ennyi: " + sizeMismatch.size() + "/" + wrong.size() + "/" + backupData.size());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
