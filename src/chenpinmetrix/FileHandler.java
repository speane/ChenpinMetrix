/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chenpinmetrix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Evgeny
 */
public class FileHandler {
    public static String readFromFile(String fileName) {
        StringBuilder result = new StringBuilder();
        String tempStr = "";
        try {
            FileReader file = new FileReader(fileName);
            BufferedReader textReader = new BufferedReader(file);
            while (textReader.ready()) {
                result.append(textReader.readLine());
                result.append('\n');
            }
        } catch(FileNotFoundException fnfe) {
            System.out.println("FILE not found");
        } catch(IOException ioe) {
            System.out.println("IO");
        }
        return new String(result);
    }
}
