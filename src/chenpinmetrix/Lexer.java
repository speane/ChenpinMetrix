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
public class Lexer {

    Lexer(String text) {
        this.sourceCode = text;
        this.tempSymbolNumber = 0;
    }

    private final String wordStartSymbols = "abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final String wordSymbols = wordStartSymbols
            + "0123456789";

    private final String sourceCode;
    private int tempSymbolNumber;
    public String lastLexem;
    public String prevLexem;
    
    public String getLastLexem() {
        return lastLexem;
    }

    private boolean isWordStartSymbol(String symbol) {
        return wordStartSymbols.contains(symbol);
    }

    private boolean isWordSymbol(String symbol) {
        return wordSymbols.contains(symbol);
    }

    private String word() {
        StringBuilder result;
        result = new StringBuilder("");

        boolean end = false;
        String nextSymbol;

        while (!end && tempSymbolNumber < sourceCode.length()) {
            nextSymbol = showNextSymbol();

            if (isWordSymbol(nextSymbol)) {
                result.append(getNextSymbol());
            } else {
                end = true;
            }
        }

        return new String(result);
    }

    private final String OPEN_CURLY_BRACE = "{";
    private final String CLOSE_CURLY_BRACE = "}";
    private final String OPEN_ROUND_BRACE = "(";
    private final String CLOSE_ROUND_BRACE = ")";
    private final String BREAKER = ";";
    private final String COMMA = ",";
    private final String QUOTES = "\"";
    private final String NEW_LINE = "\n";
    private final String SLASH = "/";
    private final String ASTERISK = "*";
    private final String APOSTROPHE = "\'";
    private final String ASSIGNMENT = "=";
    private final String OPEN_SQUARE_BRACE = "[";
    private final String CLOSE_SQUARE_BRACE = "]";
    private final String PLUS = "+";
    private final String MINUS = "-";

    private boolean isQuotes(String symbol) {
        return symbol.equals("\"");
    }

    private boolean isNewLine(String symbol) {
        return symbol.equals("\n");
    }

    private boolean isSlash(String symbol) {
        return symbol.equals("/");
    }

    private boolean isAsterisk(String symbol) {
        return symbol.equals("*");
    }

    private boolean isApostrophe(String symbol) {
        return symbol.equals("\'");
    }

    private boolean isAssignment(String symbol) {
        return symbol.equals("=");
    }

    private boolean isPlus(String symbol) {
        return symbol.equals("+");
    }

    private boolean isMinus(String symbol) {
        return symbol.equals("-");
    }

    private boolean isBlank(String symbol) {
        return symbol.equals("");
    }

    private String plus() {
        String tempSymbol;
        String result;
        tempSymbol = showNextSymbol();

        if (isPlus(tempSymbol)) {
            getNextSymbol();
            result = "++";
        } else if (isAssignment(tempSymbol)) {
            getNextSymbol();
            result = "=";
        } else {
            result = "";
        }
        return result;
    }

    private String minus() {
        String tempSymbol;
        String result;
        tempSymbol = showNextSymbol();

        if (isMinus(tempSymbol)) {
            getNextSymbol();
            result = "--";
        } else if (isAssignment(tempSymbol)) {
            getNextSymbol();
            result = "=";
        } else {
            result = "";
        }
        return result;
    }

    private void charLiteral() {
        String tempSymbol;

        while (isAvailable()) {
            tempSymbol = getNextSymbol();

            if (isApostrophe(tempSymbol)) {
                return;
            }
        }
    }

    private void stringLiteral() {
        String tempSymbol;

        while (isAvailable()) {
            tempSymbol = getNextSymbol();

            if (isQuotes(tempSymbol)) {
                return;
            }
        }
    }

    private void singleCommentary() {
        String tempSymbol;

        while (isAvailable()) {
            tempSymbol = getNextSymbol();

            if (isNewLine(tempSymbol)) {
                return;
            }
        }
    }

    private void commentaryStart() {
        String tempSymbol;

        if (isAvailable()) {
            tempSymbol = showNextSymbol();
            if (isSlash(tempSymbol)) {
                getNextSymbol();
                singleCommentary();
            } else if (isAsterisk(tempSymbol)) {
                getNextSymbol();
                multipleCommentary();
            }
        }
    }

    private void multipleCommentary() {
        String tempSymbol;

        while (isAvailable()) {
            tempSymbol = getNextSymbol();

            if (isAsterisk(tempSymbol)) {
                if (isAvailable()) {
                    tempSymbol = getNextSymbol();
                    if (isSlash(tempSymbol)) {
                        return;
                    }
                }
            }
        }
    }

    private String equalsStart() {
        if (isAssignment(showNextSymbol())) {
            getNextSymbol();
            return "";
        } else {
            return "=";
        }
    }

    private String slash() {
        if (isAssignment(showNextSymbol())) {
            getNextSymbol();
            return "=";
        } else {
            commentaryStart();
            return "";
        }
    }

    private String blank() {
        String result = "";
        String tempSymbol;
        boolean allowed = true;

        while (isAvailable() && allowed) {
            tempSymbol = showNextSymbol();
            if (isWordStartSymbol(tempSymbol)) {
                result = word();
                allowed = false;
            } else {
                switch (tempSymbol) {
                    case OPEN_CURLY_BRACE:
                    case CLOSE_CURLY_BRACE:
                    case OPEN_ROUND_BRACE:
                    case CLOSE_ROUND_BRACE:
                    case OPEN_SQUARE_BRACE:
                    case CLOSE_SQUARE_BRACE:
                    case BREAKER:
                    case NEW_LINE:
                    case COMMA:
                        result = getNextSymbol();
                        allowed = false;
                        break;
                    case QUOTES:
                        getNextSymbol();
                        stringLiteral();
                        break;
                    case SLASH:
                        getNextSymbol();
                        result = slash();
                        if (!isBlank(result)) {
                            allowed = false;
                        }
                        break;
                    case APOSTROPHE:
                        getNextSymbol();
                        charLiteral();
                        break;
                    case PLUS:
                        getNextSymbol();
                        result = plus();
                        if (!isBlank(result)) {
                            allowed = false;
                        }
                        break;
                    case MINUS:
                        getNextSymbol();
                        result = minus();
                        if (!isBlank(result)) {
                            allowed = false;
                        }
                        break;
                    case ASSIGNMENT:
                        getNextSymbol();
                        result = equalsStart();
                        allowed = false;
                        break;
                    case ASTERISK:
                        getNextSymbol();
                        result = asterisk();
                        if (!isBlank(result)) {
                            allowed = false;
                        }
                        break;
                    default:
                        getNextSymbol();
                        lastLexem = prevLexem;
                        prevLexem = "";
                        break;
                }
            }
        }
        lastLexem = prevLexem;
        prevLexem = result;
 
        return result;

    }

    private String asterisk() {
        if (isAssignment(showNextSymbol())) {
            getNextSymbol();
            return "=";
        } else {
            return "";
        }
    }

    public boolean isAvailable() {
        return tempSymbolNumber < sourceCode.length();
    }

    public String getNextLexem() {
        return blank();
    }

    private String getNextSymbol() {
        tempSymbolNumber++;
        return sourceCode.substring(tempSymbolNumber - 1, tempSymbolNumber);
    }

    private String showNextSymbol() {
        return sourceCode.substring(tempSymbolNumber, tempSymbolNumber + 1);
    }

    public String showNextLexem() {
        String result;
        String savePrevLexem = prevLexem;
        String saveLastLexem = lastLexem;
        int saveNumber = tempSymbolNumber;
        result = blank();
        prevLexem = savePrevLexem;
        lastLexem = saveLastLexem;
        tempSymbolNumber = saveNumber;
        return result;
    }
}
