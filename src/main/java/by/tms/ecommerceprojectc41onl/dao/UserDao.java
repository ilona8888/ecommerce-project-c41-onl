package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
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

    private static final String SAVE_QUERY = "INSERT INTO users (user_name, email, password_hash, status, first_name, last_name, birthday, role) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_QUERY = "UPDATE users SET status=?, first_name=?, last_name=?, birthday=?, role=?, password_hash=? WHERE id=?";


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

    public User save(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setBoolean(4, user.isStatus());
            preparedStatement.setString(5, user.getFirstName());
            preparedStatement.setString(6, user.getLastName());
            preparedStatement.setObject(7, user.getBirthday());
            preparedStatement.setString(8, user.getRole().toString());

            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Не удалось получить сгенерированный id.");
                }
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения нового пользователя.", e);
        }
    }

    public void update(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {

            preparedStatement.setBoolean(1, user.isStatus());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setObject(4, user.getBirthday());
            preparedStatement.setString(5, user.getRole().toString());
            preparedStatement.setString(6, user.getPasswordHash());
            preparedStatement.setLong(7, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления пользователя.", e);
        }
    }


}
