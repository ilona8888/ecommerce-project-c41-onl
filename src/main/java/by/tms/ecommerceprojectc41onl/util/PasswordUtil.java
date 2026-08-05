package by.tms.ecommerceprojectc41onl.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Хэширует исходный пароль с использованием алгоритма BCrypt.
     */
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Проверяет, соответствует ли введенный пароль сохраненному хэшу.
     */
    public static boolean verify(String password, String hashed) {
        try {
            return BCrypt.checkpw(password, hashed);
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}