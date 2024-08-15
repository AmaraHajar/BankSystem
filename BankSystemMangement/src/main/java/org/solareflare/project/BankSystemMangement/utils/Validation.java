package org.solareflare.project.BankSystemMangement.utils;

import org.solareflare.project.BankSystemMangement.constants.Patterns;
import org.solareflare.project.BankSystemMangement.entities.Address;
import org.solareflare.project.BankSystemMangement.exceptions.InvalidAddressException;
import org.solareflare.project.BankSystemMangement.exceptions.InvalidEmailException;
import org.solareflare.project.BankSystemMangement.exceptions.InvalidIdException;
import org.solareflare.project.BankSystemMangement.exceptions.InvalidNameException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static Pattern pattern;
    private static Matcher matcher;

    /**
     * Validates the ID number.
     *
     * @param id The ID number to validate.
     * @throws InvalidIdException If the ID number is invalid.
     */
    public static void validateIdNumber(String id) throws InvalidIdException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidIdException("ID number cannot be null or empty.");
        }
        if (!isValid("ID", id)) {
            throw new InvalidIdException("Invalid ID number format.");
        }
    }

    /**
     * Validates the address.
     *
     * @param address The address to validate.
     * @throws InvalidAddressException If the address is invalid.
     */
    public static void validateAddress(Address address) throws InvalidAddressException {
        if (address == null) {
            throw new InvalidAddressException("Address cannot be null.");
        }
        if (address.getStreet() == null || address.getStreet().trim().isEmpty()) {
            throw new InvalidAddressException("Street cannot be null or empty.");
        }
        if (address.getCity() == null || address.getCity().trim().isEmpty()) {
            throw new InvalidAddressException("City cannot be null or empty.");
        }
        if (address.getState() == null || address.getState().trim().isEmpty()) {
            throw new InvalidAddressException("State cannot be null or empty.");
        }
    }

    /**
     * Validates a name (first name or last name).
     *
     * @param type The type of name (e.g., "First Name", "Last Name").
     * @param name The name to validate.
     * @throws InvalidNameException If the name is invalid.
     */
    public static void validateName(String type, String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException(type + " cannot be null or empty.");
        }
        if (!isValid(type.toUpperCase(), name)) {
            throw new InvalidNameException("Invalid " + type + " format.");
        }
    }

    /**
     * Validates the email address.
     *
     * @param email The email address to validate.
     * @throws InvalidEmailException If the email address is invalid.
     */
    public static void validateEmail(String email) throws InvalidEmailException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailException("Email cannot be null or empty.");
        }
        if (!isValid("EMAIL", email)) {
            throw new InvalidEmailException("Invalid email format.");
        }
    }

    /**
     * Helper method to validate strings against specific patterns.
     *
     * @param type  The type of validation (e.g., "EMAIL", "NAME", "ID").
     * @param value The string value to validate.
     * @return True if the value matches the pattern; false otherwise.
     */
    private static boolean isValid(String type, String value) {
        switch (type.toUpperCase()) {
            case "EMAIL":
                pattern = Pattern.compile(Patterns.EMAILPATTERN);
                break;
            case "FIRST NAME":
            case "LAST NAME":
                pattern = Pattern.compile(Patterns.NAMEPATTERN);
                break;
            case "ID":
                pattern = Pattern.compile(Patterns.IDPATTERN);
                break;
            default:
                throw new IllegalArgumentException("Unknown validation type: " + type);
        }

        matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * Validates user details collectively.
     *
     * @param idNumber  The user's ID number.
     * @param firstName The user's first name.
     * @param lastName  The user's last name.
     * @param email     The user's email address.
     * @throws InvalidIdException       If the ID number is invalid.
     * @throws InvalidNameException     If the first or last name is invalid.
     * @throws InvalidEmailException    If the email address is invalid.
     */
    public static void validateUserDetails(String idNumber, String firstName, String lastName, String email)
            throws InvalidIdException, InvalidNameException, InvalidEmailException {
        validateIdNumber(idNumber);
        validateName("First Name", firstName);
        validateName("Last Name", lastName);
        validateEmail(email);
    }
}
