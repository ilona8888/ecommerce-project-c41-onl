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
import java.util.Optional;

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

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM USERS WHERE ID = ?";

    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM USERS WHERE EMAIL=?;";

    private static final String UPDATE_ROLE_QUERY = "UPDATE USERS SET ROLE = ? WHERE ID = ?";

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
            throw new RuntimeException("Ошибка поиска пользователя", e);
        }
    }

    private User mapToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("ID"),
                resultSet.getString("USER_NAME"),
                resultSet.getString("EMAIL"),
                resultSet.getString("PASSWORD_HASH"),
                resultSet.getBoolean("STATUS"),
                resultSet.getString("FIRST_NAME"),
                resultSet.getString("LAST_NAME"),
                resultSet.getDate("BIRTHDAY").toLocalDate(),
                UserRole.valueOf(resultSet.getString("ROLE").trim().toUpperCase())
        );
    }

    public Optional<User> getById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToUser(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя", e);
        }
    }

    public Optional<User> findByEmail(String email) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_QUERY)) {

            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToUser(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя по email: " + email, e);
        }

    }

    public void updateRole(long id, UserRole userRole) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ROLE_QUERY)) {

            preparedStatement.setString(1, userRole.name());
            preparedStatement.setLong(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении роли для пользователя с ID: " + id, e);
        }
    }
}
