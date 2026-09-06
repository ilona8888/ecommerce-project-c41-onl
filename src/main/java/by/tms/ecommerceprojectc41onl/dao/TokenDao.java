package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TokenDao {

    private static final String SAVE_SQL = "INSERT INTO TOKENS (ID, TYPE, IS_ACTIVE, USER_ID) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT ID, TYPE, IS_ACTIVE, USER_ID, CREATED_DATE FROM TOKENS WHERE ID = ?";
    private static final String DEACTIVATE = "UPDATE TOKENS SET IS_ACTIVE = FALSE WHERE ID = ?";

    @Autowired
    private UserDao userDao;
    private final DataSource dataSource;

    public Token save(Token token) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {

            preparedStatement.setObject(1, token.getId());
            preparedStatement.setString(2, token.getType());
            preparedStatement.setBoolean(3, token.isActive());
            preparedStatement.setLong(4, token.getUser().getId());
            preparedStatement.executeUpdate();

            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            return token;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении токена", e);
        }
    }

    public Optional<Token> findById(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Token token = new Token();
                token.setId(resultSet.getObject("ID", UUID.class));
                token.setType(resultSet.getString("TYPE"));
                token.setActive(resultSet.getBoolean("IS_ACTIVE"));
                token.setUser(userDao.getById((resultSet.getLong("USER_ID"))).get());
                token.setCreatedDate(resultSet.getTimestamp("CREATED_DATE").toLocalDateTime().toLocalDate());

                return Optional.of(token);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске токена по ID", e);
        }
        return Optional.empty();
    }

    public void deactivate(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DEACTIVATE)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException error) {
            throw new RuntimeException("Ошибка гашения токена", error);
        }
    }
}


