package org.pawpal.service;

import java.security.SecureRandom;

public class PasswordService {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!$&#?";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;

    private static final SecureRandom random = new SecureRandom();

    private static char getRandomChar(String characters) {
        return characters.charAt(random.nextInt(characters.length()));
    }

    private static String shufflePassword(String password) {
        char[] chars = password.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int randomIndex = random.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[randomIndex];
            chars[randomIndex] = temp;
        }
        return new String(chars);
    }

    public static String generatePassword() {
        StringBuilder password = new StringBuilder();
        password.append(getRandomChar(UPPERCASE));
        password.append(getRandomChar(LOWERCASE));
        password.append(getRandomChar(DIGITS));
        password.append(getRandomChar(SPECIAL_CHARACTERS));

        for(int i = 0; i < 4; i++)
            password.append(getRandomChar(ALL_CHARACTERS));

        return shufflePassword(password.toString());
    }

    public static boolean isValid(String password) {
        boolean containsUpperCase = false;
        boolean containsLowerCase = false;
        boolean containsDigit = false;
        boolean containsSpecial = false;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if(UPPERCASE.indexOf(ch) != -1) containsUpperCase = true;
            if(LOWERCASE.indexOf(ch) != -1) containsLowerCase = true;
            if(DIGITS.indexOf(ch) != -1) containsDigit = true;
            if(SPECIAL_CHARACTERS.indexOf(ch) != -1) containsSpecial = true;
        }
        return containsUpperCase && containsLowerCase && containsDigit && containsSpecial;
    }
}
