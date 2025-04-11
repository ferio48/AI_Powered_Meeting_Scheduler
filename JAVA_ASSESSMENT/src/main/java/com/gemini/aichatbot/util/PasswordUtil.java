package com.gemini.aichatbot.util;

import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Utility class for password generation and validation.
 *
 * This class includes methods to:
 * - Generate secure random passwords with mixed characters
 * - Compare passwords
 * - Validate password format (minimum requirements)
 */
public class PasswordUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private PasswordUtil() {}

    // Character categories used for password generation
    static char[] SYMBOLS = "^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
    static char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    static char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    static char[] NUMBERS = "0123456789".toCharArray();
    static char[] ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();

    // Secure random generator
    static Random rand = new SecureRandom();

    /**
     * Generates a secure random password of the specified length.
     *
     * The generated password includes:
     * - At least one lowercase letter
     * - At least one uppercase letter
     * - At least one digit
     * - At least one special symbol
     *
     * @param length the total length of the password to generate (minimum 4)
     * @return the generated password as a {@link String}
     */
    public static String getPassword(int length) {
        char[] password = new char[length];

        // Ensure inclusion of minimum required characters
        password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
        password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
        password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
        password[3] = SYMBOLS[rand.nextInt(SYMBOLS.length)];

        // Fill remaining characters randomly
        for (int i = 4; i < length; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        // Shuffle password to randomize character positions
        for (int i = 0; i < password.length; i++) {
            int randomPosition = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }

        return new String(password);
    }

    /**
     * Compares two passwords for equality.
     *
     * @param firstPassword the first password to compare
     * @param secondPassword the second password to compare
     * @return {@code true} if both passwords are equal, {@code false} otherwise
     */
    public static boolean isFirstPasswordSameAsSecondPassword(final String firstPassword, final String secondPassword) {
        return StringUtils.equals(firstPassword, secondPassword);
    }

    /**
     * Validates whether the given password meets basic format rules.
     *
     * Password must:
     * - Be at least 8 characters long
     * - Contain at least one digit
     * - Contain at least one lowercase letter
     * - Contain at least one uppercase letter
     *
     * @param password the password to validate
     * @return {@code true} if password does NOT meet the format requirements, {@code false} if it does
     */
    public static boolean isNotInPasswordFormat(String password) {
        if (password == null) {
            return false;
        }

        String passwordPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$";

        return !password.matches(passwordPattern);
    }
}