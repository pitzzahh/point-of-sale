package com.pos.validation;

import java.io.*;

/**
 * Class used to check whether the products have been added to the table in the database.
 * Also used for some input checking.
 * Used to fix the bug that makes the program add more products to the database every time the program is re-executed.
 * @author peter
 */
public class Checker {

    public static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (RuntimeException ignored) {}
        return false;
    }
    
    public static boolean isNumberWithDecimal(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (RuntimeException ignored) {}
        return false;
    }
}
