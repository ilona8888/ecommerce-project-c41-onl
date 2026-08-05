package by.tms.ecommerceprojectc41onl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    private static final String VALID_PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*\\(\\),\\.\\?\\\":\\{\\}\\|<>_])[A-Za-z\\d!@#\\$%\\^&\\*\\(\\),\\.\\?\\\":\\{\\}\\|<>_]{8,20}$";

    public boolean isValid(String password){
        if(password == null){
            return false;
        }
        Pattern pattern = Pattern.compile(VALID_PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
