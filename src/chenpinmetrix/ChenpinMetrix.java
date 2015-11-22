/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chenpinmetrix;

/**
 *
 * @author Evgeny
 */

import java.util.ArrayList;

public class ChenpinMetrix {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        ArrayList<Variable> variables = new ArrayList<>();
        
        String sourceCode = FileHandler.readFromFile("source.js");
        SyntaxAnalyzer codeAnalyzer = new SyntaxAnalyzer(sourceCode);
        variables = codeAnalyzer.analyze();
        
        for (Variable tempVariable : variables) {
            System.out.println(tempVariable.name + " " + tempVariable.input + " " +
                                tempVariable.managing + " " + 
                                tempVariable.modifiable + " " + 
                                tempVariable.parasitic);
        }
    }
    
}
