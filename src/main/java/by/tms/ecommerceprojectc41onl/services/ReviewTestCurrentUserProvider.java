package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.UserDao;
import by.tms.ecommerceprojectc41onl.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Временный поставщик текущего пользователя для проверки отзывов.
 * Заменить реализацией на основе настоящей авторизации, когда она появится в проекте.
 */
@Component
public class ReviewTestCurrentUserProvider implements ReviewCurrentUserProvider {

    private static final String USER_NOT_FOUND_MESSAGE = "Не найден пользователь по имени пользователя";

    private final UserDao userDao;
    private final String testUsername;

    public ReviewTestCurrentUserProvider(UserDao userDao,
                                         @Value("${REVIEW_TEST_USERNAME:}") String testUsername) {
        this.userDao = userDao;
        this.testUsername = testUsername;
    }

    @Override
    public User getCurrentUser() {
        if (testUsername == null || testUsername.isBlank()) {
            return null;
        }

        try {
            return userDao.getByName(testUsername.trim());
        } catch (RuntimeException exception) {
            if (exception.getMessage() != null
                    && exception.getMessage().startsWith(USER_NOT_FOUND_MESSAGE)) {
                return null;
            }
            throw exception;
        }
    }
}
