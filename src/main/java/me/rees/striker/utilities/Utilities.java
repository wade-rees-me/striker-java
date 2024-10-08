package me.rees.striker.utilities;

public class Utilities {

    // Method to add commas to an integer number
    public static String addCommas(long number) {
        String str = Long.toString(number);
        int insertPosition = str.length() - 3;

        while (insertPosition > 0) {
            str = str.substring(0, insertPosition) + "," + str.substring(insertPosition);
            insertPosition -= 3;
        }

        return str;
    }
}

