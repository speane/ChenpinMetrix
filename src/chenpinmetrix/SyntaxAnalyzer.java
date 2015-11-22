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

public class SyntaxAnalyzer {
    
    
    private final int IDENTIFIER = 1;
    private final int FUNCTION = 8;
    private final int OPEN_CURLY = 4;
    private final int CLOSE_CURLY = 5;
    private final int OPEN_ROUND = 6;
    private final int CLOSE_ROUND = 7;
    private final int BREAKER = 3;
    private final int RUBBISH = 0;
    private final int COMMA = 9;
    private final int FOR = 10;
    private final int IF = 11;
    private final int WHILE = 12;
    private final int VAR = 13;
    private final int NEW_LINE = 2;
    private final int ASSIGNMENT = 14;
    private final int OPEN_SQUARE = 15;
    private final int CLOSE_SQUARE = 16;
    
    
    
    
    private int currentDepthLevel;
    private ArrayList<Variable> variables;
    private final Lexer mainLexer;
    private Variable lastVariable;
    
    public SyntaxAnalyzer(String sourceCode) {
        this.mainLexer = new Lexer(sourceCode);
        this.variables = new ArrayList<>();
        this.currentDepthLevel = 0;
        this.lastVariable = null;
    }
    
    public ArrayList<Variable> analyze() {
        String tempLexem;
        
        while (mainLexer.isAvailable()) {
            tempLexem = mainLexer.getNextLexem();
            switch (getLexemType(tempLexem)) {
                case VAR:
                    varLexem();
                    break;
                case IDENTIFIER:
                    identifierLexem(tempLexem);
                    break;
                case FUNCTION:
                    functionLexem();
                    break;
                case ASSIGNMENT:
                    assignmentLexem();
                    break;
            }
        }
        
        return this.variables;
    }
    
    private void assignmentLexem() {
        lastVariable.modifiable = true;
        lastVariable.input = false;
        lastVariable.parasitic = false;
    }
    
    private void functionLexem() {
        String tempLexem = mainLexer.getNextLexem();
        
        if (getLexemType(tempLexem) == IDENTIFIER) {
            tempLexem = mainLexer.getNextLexem();
            if (getLexemType(tempLexem) == OPEN_ROUND) {
                functionParameters();
            }
        }
    }
    
    private void functionParameters() {
        String tempLexem = mainLexer.getNextLexem();
        while (getLexemType(tempLexem) != CLOSE_ROUND) {
            if (getLexemType(tempLexem) == IDENTIFIER) {
                    addNewVariable(tempLexem, (currentDepthLevel + 1));
                    lastVariable.input = true;
            }
            tempLexem = mainLexer.getNextLexem();
            
        }
    }
    
    private void varLexem() {
        String tempLexem = mainLexer.getNextLexem();
        boolean nextAllowed = true;
        /*if (getLexemType(tempLexem) == IDENTIFIER) {
            addNewVariable(tempLexem, currentDepthLevel);
        }
        else {
            
        }*/
        
        while (mainLexer.isAvailable() && (getLexemType(tempLexem) != NEW_LINE) &&
                (getLexemType(tempLexem) != BREAKER)) {
            switch (getLexemType(tempLexem)) {
                case IDENTIFIER:
                    if (nextAllowed) {
                        addNewVariable(tempLexem, currentDepthLevel);
                    }
                    nextAllowed = false;
                    break;
                case COMMA:
                    nextAllowed = true;
                    break;
                
            }
            tempLexem = mainLexer.getNextLexem();
        }
        
    }
    
    
    
    private void identifierLexem(String identifier) {
        for (Variable tempVariable : variables) {
            if (tempVariable.name.equals(identifier)) {
                lastVariable = tempVariable;
                lastVariable.parasitic = false;
                break;
            }
        }
    }
    
    private void addNewVariable(String name, int depthLevel) {
        for (Variable tempVariable : variables) {
            if (tempVariable.name.equals(name)) {
                tempVariable.active = false;
                break;
            }
        }
        
        Variable newVariable = new Variable(name, depthLevel);
        variables.add(newVariable);
        lastVariable = newVariable;
    }
    
    private int getLexemType(String lexem) {
        
        switch (lexem) {
            case "" :
                return RUBBISH;
            case "{" :
                return OPEN_CURLY;
            case "}" :
                return CLOSE_CURLY;
            case ";" :
                return BREAKER;
            case "(" :
                return OPEN_ROUND;
            case ")" :
                return CLOSE_ROUND;
            case "function" :
                return FUNCTION;
            case "," :
                return COMMA;
            case "if" :
                return IF;
            case "for":
                return FOR;
            case "while":
                return WHILE;
            case "var" :
                return VAR;
            case "\n":
                return NEW_LINE;
            case "=":
                return ASSIGNMENT;
            case "[":
                return OPEN_SQUARE;
            case "]":
                return CLOSE_SQUARE;
            default:
                return IDENTIFIER;
        }
    }
}