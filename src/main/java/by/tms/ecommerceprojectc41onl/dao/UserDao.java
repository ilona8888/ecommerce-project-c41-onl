package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO для работы с пользователями.
 *
 * @author Ирина Мизгир
 * @date 16.08.2026 19:25
 */

@Repository
@RequiredArgsConstructor
public class UserDao {

    private static final String SELECT_BY_USER_NAME_QUERY = "SELECT * FROM USERS WHERE USER_NAME = ?";

    private final DataSource dataSource;

    /**
     * Получение пользователя по имени.
     *
     * @param userName Имя пользователя.
     * @return Пользователь.
     */
    public User getByName(String userName) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_USER_NAME_QUERY)) {

            preparedStatement.setString(1, userName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToUser(resultSet);
                }

                throw new RuntimeException("Не найден пользователь по имени пользователя %s".formatted(userName));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска продавца", e);
        }
    }

    private User mapToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("ID"),
                resultSet.getString("USER_NAME"),
                resultSet.getString("EMAIL"),
                resultSet.getString("PASSWORD_HASH"),
                resultSet.getString("FIRST_NAME"),
                resultSet.getString("LAST_NAME"),
                resultSet.getDate("BIRTHDAY").toLocalDate(),
                UserRole.valueOf(resultSet.getString("ROLE"))
        );
    }
}
