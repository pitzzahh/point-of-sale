package com.github.pitzzahh.validation;

/**
 * Class used to check whether the products have been added to the table in the database.
 * Also used for some input checking.
 * Used to fix the bug that makes the program add more products to the database every time the program is re-executed.
 * @author peter
 */
public class Checker {

    /**
     * Method that checks if a {@code String} is an Integer
     * @param string the {@code String} to check.
     * @return {@code true} if the {@code String} is an {@code Integer} , false otherwise.
     */
    public static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (RuntimeException ignored) {}
        return false;
    }

    /**
     * Method that checks if a {@code String} is an Double
     * @param string the {@code String} to check.
     * @return {@code true} if the {@code String} is a {@code Double} , false otherwise.
     */
    public static boolean isNumberWithDecimal(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (RuntimeException ignored) {}
        return false;
    }
}
