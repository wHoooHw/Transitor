package main.hellofx;

import java.util.regex.Pattern;

public class PostcodeValidator {

    private static final String POSTCODE_REGEX = "\\d{4}[a-zA-Z]{2}";

    public static boolean isValid(String postcode) {
        return Pattern.matches(POSTCODE_REGEX, postcode);
    }
}
