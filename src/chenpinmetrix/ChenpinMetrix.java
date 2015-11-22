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
        
        int P = 0, M = 0, C = 0, T = 0;
        
        for (Variable tempVariable : variables) {
            
            if (tempVariable.input) {
                P++;
            }
            if (tempVariable.modifiable) {
                M++;
            }
            if (tempVariable.managing) {
                C++;
            }
            if (tempVariable.parasitic) {
                T++;
            }
            
            /*System.out.println(tempVariable.name + " " + tempVariable.input + " " +
                                tempVariable.managing + " " + 
                                tempVariable.modifiable + " " + 
                                tempVariable.parasitic);*/
        }
        
        System.out.println("P + 2*M + 3*C + 0.5*T =\n" + P + " + 2 * " + M + 
                            " + 3 * " + C + " + 0.5 * " + T + " = " + 
                            (P + 2 * M + 3 * C + 0.5 * T));
    }
    
}
