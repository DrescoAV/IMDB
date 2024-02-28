package org.example;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Credentials {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 15;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private String email;
    private String password;

    public Credentials(String email) {
        this.email = email;
        this.password = generateStrongPassword();
    }

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private String generateStrongPassword() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int character = (int) (random.nextFloat() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        IMDB imdb = IMDB.getInstance();
        return (matcher.matches() && !imdb.doesEmailExist(email));
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
