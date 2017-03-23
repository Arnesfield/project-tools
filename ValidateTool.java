package tools;

/**
 * Do validation-related events with this class tool.
 * @author Arnesfield
 */
public final class ValidateTool {
    
    private ValidateTool() {}
    
    /**
     * Custom validation.
     * @param regex regular Expression for matching
     * @param expr expression to be validated
     * @return true if expr matches regex; otherwise, false
     */
    public static final boolean is(String regex, String expr) {
        return expr.matches(regex);
    }
    
    /**
     * Determines if expr is an integer.
     * @param expr expression to be validated
     * @return true if expr is an integer; otherwise, false
     */
    public static final boolean isInt(String expr) {
        try {
            Integer.parseInt(expr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Determines if expr is an integer.
     * @param expr expression to be validated
     * @param positive true if expr should be positive; otherwise, false
     * @param zero true if expr should consider zero (0); otherwise, false
     * @return true if expr is an integer; otherwise, false
     */
    public static final boolean isInt(String expr, boolean positive, boolean zero) {
        try {
            int n = Integer.parseInt(expr);
            return ((positive) ? n > 0 : n < 0) || ((zero) ? n == 0 : false);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Determines if expr is an integer.
     * @param expr expression to be validated
     * @param rOperator relational operator to be performed on expr and n
     * @param n integer in the equation
     * @return true if expr is an integer and if condition is met; otherwise, false
     */
    public static final boolean isInt(String expr, String rOperator, int n) {
        try {
            int m = Integer.parseInt(expr);
            return (rOperator.equals("==")) ? m == n
                    : (rOperator.equals("!=")) ? m != n
                    : (rOperator.equals("<")) ? m < n
                    : (rOperator.equals(">")) ? m > n
                    : (rOperator.equals("<=")) ? m <= n
                    : (rOperator.equals(">=")) ? m >= n
                    : false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Determines if expr is composed of letters in the alphabet.
     * @param expr expression to be validated
     * @return true if expr is composed of letters in the alphabet; otherwise, false
     */
    public static final boolean isAlphabet(String expr) {
        return isAlphabet(expr, true, true, false);
    }
    
    /**
     * Determines if expr is composed of letters in the alphabet.
     * @param expr expression to be validated
     * @param spaces true if expr may have spaces; otherwise, false
     * @return true if expr is composed of letters in the alphabet; otherwise, false
     */
    public static final boolean isAlphabet(String expr, boolean spaces) {
        return isAlphabet(expr, true, true, spaces);
    }
    
    /**
     * Determines if expr is composed of letters in the alphabet.
     * @param expr expression to be validated
     * @param lower true if expr may have lowercase letters; otherwise, false
     * (at least one of parameters (lower and upper) should be true)
     * @param upper true if expr may have uppercase letters; otherwise, false
     * (at least one of parameters (lower and upper) should be true)
     * @param spaces true if expr may have spaces; otherwise, false
     * @return true if expr is composed of letters in the alphabet; otherwise, false
     */
    public static final boolean isAlphabet(String expr, boolean lower, boolean upper, boolean spaces) {
        if (!(lower && upper))
            return false;
        
        String regex = ((spaces) ? " " : "")
                + ((lower && upper) ? "a-zA-Z"
                : (lower) ? "a-z"
                : (upper) ? "A-Z"
                : "");
        return expr.matches("[" + regex + "]+");
    }
    
}
