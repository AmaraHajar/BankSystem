package org.solareflare.project.BankSystemMangement.utils;

import org.solareflare.project.BankSystemMangement.beans.Address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static Pattern pattern;
    private static Matcher matcher;
    private static String toBeMach;

    public static boolean isValidIdNumber(String id) {
        toBeMach = "ID";
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return isValid("id",id);
    }

    public static boolean isValidAddress(Address address) {
        if (address == null)
            return false;
        if (address.getStreet() == null || address.getStreet().isEmpty())
            return false;
        if (address.getCity() == null || address.getCity().isEmpty())
            return false;
        if (address.getState() == null || address.getState().isEmpty())
            return false;
        return true;
    }

    /**
     * @param name
     * @param lastName
     * @return
     */
    public static boolean isValidName(String name, String lastName) {
        toBeMach = "NAME";
        boolean flag = isValid("name",name);
        if (flag) {
            toBeMach = "LASTNAME";
            flag = isValid("lastname",lastName);
        }
        return flag;
    }


    /**
     * Method to validate email address
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        return isValid("EMAIL",email);

    }

    /**
     * @param name
     * @return
     */
    private static boolean isValid(String name, String s) {

        switch (name.toUpperCase()) {
            case "EMAIL":
                pattern = Pattern.compile(Patterns.EMAILPATTERN);
                break;
            case "NAME":
            case "LASTNAME":
                pattern = Pattern.compile(Patterns.NAMEPATTERN);
                break;
            case "ID":
                pattern = Pattern.compile(Patterns.IDPATTERN);
                break;
        }
        if (s.isEmpty())
            return false;
        matcher = pattern.matcher(s);

        return matcher.matches();
    }
}
