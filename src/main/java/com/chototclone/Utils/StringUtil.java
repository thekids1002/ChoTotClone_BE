package com.chototclone.Utils;

import java.security.SecureRandom;
import java.util.Base64;

public class StringUtil {

    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a random alphanumeric string of the specified length.
     *
     * @param length the length of the generated string (must be greater than 0)
     * @return a randomly generated alphanumeric string of the specified length
     * @throws IllegalArgumentException if the length is less than 1
     */
    public static String generateRandomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHANUMERIC_CHARACTERS.length());
            sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str the string to check
     * @return true if the string is null or empty; false otherwise
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if a string is null, empty, or contains only whitespace.
     *
     * @param str the string to check
     * @return true if the string is null, empty, or whitespace; false otherwise
     */
    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Trims a string and returns an empty string if it is null.
     *
     * @param str the string to trim
     * @return the trimmed string, or an empty string if the input is null
     */
    public static String safeTrim(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * Encodes a string to Base64 format.
     *
     * @param input the string to encode
     * @return the Base64 encoded string
     */
    public static String encodeToBase64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    /**
     * Decodes a Base64 encoded string.
     *
     * @param encoded the Base64 encoded string
     * @return the decoded string
     */
    public static String decodeFromBase64(String encoded) {
        return new String(Base64.getDecoder().decode(encoded));
    }

    /**
     * Capitalizes the first letter of a string.
     *
     * @param str the string to capitalize
     * @return the string with the first letter capitalized
     */
    public static String capitalizeFirstLetter(String str) {
        if (isNullOrBlank(str)) {
            return str;
        }

        // Convert the first character to uppercase and concatenate it with the rest of the string
        char firstChar = Character.toUpperCase(str.charAt(0));
        return firstChar + str.substring(1);
    }

    /**
     * Reverses a string.
     *
     * @param str the string to reverse
     * @return the reversed string
     */
    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Converts a string to camel case.
     *
     * @param str the string to convert
     * @return the camel-cased string
     */
    public static String toCamelCase(String str) {
        if (isNullOrBlank(str)) {
            return str;
        }
        StringBuilder camelCaseString = new StringBuilder();
        boolean nextIsUpper = false;
        for (char c : str.toCharArray()) {
            if (c == ' ' || c == '-') {
                nextIsUpper = true;
            } else {
                if (nextIsUpper) {
                    camelCaseString.append(Character.toUpperCase(c));
                    nextIsUpper = false;
                } else {
                    camelCaseString.append(Character.toLowerCase(c));
                }
            }
        }
        return camelCaseString.toString();
    }
}
