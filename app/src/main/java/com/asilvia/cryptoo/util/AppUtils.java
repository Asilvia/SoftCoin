package com.asilvia.cryptoo.util;

/**
 * Created by asilvia on 04/06/2018.
 */

public class AppUtils {

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
