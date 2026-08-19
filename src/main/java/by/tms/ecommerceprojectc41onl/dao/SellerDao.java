package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO для работы с продавцами.
 *
 * @author Ирина Мизгир
 * @date 16.08.2026 18:51
 */
@Repository
@RequiredArgsConstructor
public class SellerDao {

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM SELLERS WHERE id = ?";

    private final DataSource dataSource;

    /**
     * Получение продавца по идентификатору.
     *
     * @param id идентификатор продавца
     * @return продавец
     */
    public Seller getById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToSeller(resultSet);
                }

                throw new RuntimeException("Не найден пользователь по id %s".formatted(id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска продавца", e);
        }
    }

    private Seller mapToSeller(ResultSet resultSet) throws SQLException {
        var seller = new Seller();
        seller.setId(resultSet.getLong("USERS_ID"));
        seller.setContactInfo(resultSet.getString("CONTACT_INFO"));
        seller.setDetails(resultSet.getString("DETAILS"));

        return seller;
    }
}
