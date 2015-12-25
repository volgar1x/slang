package org.junit;

/**
 * @author Antoine Chauvin
 */
public class AssertFormat {
    public static String format(String message, Object expected, Object actual) {
        return Assert.format(message, expected, actual);
    }
}
