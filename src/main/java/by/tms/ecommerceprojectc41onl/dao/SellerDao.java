package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

/**
 * DAO для работы с продавцами.
 *
 * @author Ирина Мизгир
 * @date 16.08.2026 18:51
 */
@Repository
@RequiredArgsConstructor
public class SellerDao {

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM SELLERS WHERE users_id = ?";
    private static final String SELECT_DETAILS_BY_ID_QUERY = "SELECT details FROM SELLERS WHERE users_id = ?";
    private static final String SELECT_CONTACT_INFO_BY_ID_QUERY = "SELECT contact_info FROM SELLERS WHERE users_id = ?";

    private static final String UPDATE_DETAILS_BY_ID_QUERY = "UPDATE SELLERS SET details = ? WHERE users_id = ?";
    private static final String UPDATE_CONTACT_INFO_BY_ID_QUERY = "UPDATE SELLERS SET contact_info = ? WHERE users_id = ?";

    private final DataSource dataSource;

    private final UserDao userDao;

    /**
     * Получение продавца по идентификатору.
     *
     * @param userId идентификатор продавца
     * @return продавец
     */
    public Seller getByUserId(long userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_USER_ID_QUERY)) {

            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToSeller(resultSet);
                }

                throw new RuntimeException("Не найден пользователь по userId %s".formatted(userId));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска продавца", e);
        }
    }

    private Seller mapToSeller(ResultSet resultSet) throws SQLException {
        var seller = new Seller();
        long usersId = resultSet.getLong("USERS_ID");
        seller.setUser(userDao.getById(usersId));
        seller.setContactInfo(resultSet.getString("CONTACT_INFO"));
        seller.setDetails(resultSet.getString("DETAILS"));

        return seller;
    }

    /**
     * Обновление информации о продавце
     *
     * @param id              идентификатор продавца
     * @param legalEntityInfo информация
     * @return статус обновления
     */
    public boolean setInfoAboutLegalEntity(Long id, String legalEntityInfo) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DETAILS_BY_ID_QUERY)) {

            preparedStatement.setString(1, legalEntityInfo);
            preparedStatement.setLong(2, id);

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows != 1) return false;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска продавца", e);
        }
        return true;
    }

    /**
     * Получение наименования организации продавца
     *
     * @param id идентификатор продавца
     * @return наименование организации продавца
     */
    public String getInfoAboutLegalEntity(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DETAILS_BY_ID_QUERY)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }

                throw new RuntimeException("Не найден пользователь по id %s".formatted(id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска продавца", e);
        }
    }

    /**
     * Обновляет контактную информацию о продавце
     *
     * @param id идентификатор продавца
     * @param contactInfo контактная информация
     * @return статус обновления
     */
    public boolean setContactInfo(Long id, String contactInfo) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CONTACT_INFO_BY_ID_QUERY)) {

            preparedStatement.setString(1, contactInfo);
            preparedStatement.setLong(2, id);

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows != 1) return false;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска продавца", e);
        }
        return true;
    }

    /**
     * Получение контактных данных продавца
     *
     * @param id идентификатор продавца
     * @return контактные данные продавца
     */
    public String getContactInfo(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CONTACT_INFO_BY_ID_QUERY)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }

                throw new RuntimeException("Не найден пользователь по id %s".formatted(id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска продавца", e);
        }
    }
}
