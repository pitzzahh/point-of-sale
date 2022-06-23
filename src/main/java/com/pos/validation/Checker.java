package com.pos.validation;

import java.io.*;

/**
 * Class used to check whether the products have been added to the table in the database.
 * Also used for some input checking.
 * Used to fix the bug that makes the program add more products to the database every time the program is re-executed.
 * @author peter
 */
public class Checker {
    
    /**
     * Creates a file that denotes that the products have been added to the table from the database.
     */
    public static void createFile() {
        try {
            File file = new File("src/main/resources/check.txt");
            if (!file.exists()) writeToFile(file);
        } catch (IOException ignored) {}
    }

    private static void writeToFile(File file) throws IOException {
        boolean created = file.createNewFile();
        if (created) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                bufferedWriter.write("This file is used to check whether the products are in the database, just checks if this file exists.\nDO NOT REMOVE THIS FILE, IF THIS FIL IS REMOVED, THE PROGRAM WILL KEEP INSERTING PRODUCTS TO THE TABLE");
            }
        }
    }

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
