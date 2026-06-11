package com.studentrecords.util;

/**
 * Utility class for input validation.
 * Ensures data integrity before any database operation.
 */
public class Validator {

    /**
     * Validates that a string is non-null and non-blank.
     */
    public static boolean isNonEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates email format using a simple regex.
     */
    public static boolean isValidEmail(String email) {
        if (!isNonEmpty(email)) return false;
        return email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Validates a 10-digit Indian phone number.
     */
    public static boolean isValidPhone(String phone) {
        if (!isNonEmpty(phone)) return false;
        return phone.matches("^[6-9]\\d{9}$");
    }

    /**
     * Validates CGPA is between 0.00 and 10.00.
     */
    public static boolean isValidCgpa(double cgpa) {
        return cgpa >= 0.00 && cgpa <= 10.00;
    }

    /**
     * Validates year of study is between 1 and 4.
     */
    public static boolean isValidYear(int year) {
        return year >= 1 && year <= 4;
    }

    /**
     * Validates roll number format: 2-3 uppercase letters + 4-digit year + 3-digit seq.
     * Example: CS2021001, EE2022012
     */
    public static boolean isValidRollNumber(String roll) {
        if (!isNonEmpty(roll)) return false;
        return roll.matches("^[A-Z]{2,3}\\d{7}$");
    }

    /**
     * Full validation of a student object.
     * @return null if valid, or an error message string
     */
    public static String validateStudent(String roll, String firstName, String lastName,
                                          String email, String phone, int year, double cgpa) {
        if (!isValidRollNumber(roll))      return "Invalid roll number format (e.g. CS2021001).";
        if (!isNonEmpty(firstName))        return "First name is required.";
        if (!isNonEmpty(lastName))         return "Last name is required.";
        if (!isValidEmail(email))          return "Invalid email address.";
        if (!isValidPhone(phone))          return "Invalid phone number (10 digits, starts with 6-9).";
        if (!isValidYear(year))            return "Year of study must be between 1 and 4.";
        if (!isValidCgpa(cgpa))            return "CGPA must be between 0.00 and 10.00.";
        return null; // all valid
    }
}
