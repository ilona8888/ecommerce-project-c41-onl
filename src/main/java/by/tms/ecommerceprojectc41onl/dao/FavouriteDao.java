package by.tms.ecommerceprojectc41onl.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
//TODO : Предполагаю, что будут такие методы(можно что-то убрать или добавить)
public class FavouriteDao {

    private static final String SELECT_EXISTS_BY_USER_ID_AND_PRODUCTS_ID = """
            SELECT EXISTS ( SELECT 1 FROM favorites WHERE users_id = ? AND products_id = ? )""";

    private static final String INSERT_QUERY =
            "INSERT INTO favorites (users_id, products_id) VALUES (?, ?)";

    private static final String DELETE_QUERY =
            "DELETE FROM favorites WHERE users_id = ? AND products_id = ?";

    private final DataSource dataSource;

    /**
     * Удаляет товар из избранного пользователя.
     * @param userId ID пользователя
     * @param productId ID товара
     * @return количество удалённых строк (0 или 1)
     */
    // public int removeProduct(long userId, long productId){}

    /**
     * Проверяет, есть ли товар в избранном у пользователя.
     *
     * @param userId ID пользователя
     * @param productId ID товара
     * @return true, если товар в избранном, иначе false
     */
    public boolean exists(long userId, long productId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXISTS_BY_USER_ID_AND_PRODUCTS_ID)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }

                throw new RuntimeException("Не возможно получить данные об избранном товаре. id товара: %s, id пользователя %s".formatted(productId, userId));
            }
        } catch (SQLException error) {
            throw new RuntimeException("Ошибка получение данных об избранном товаре", error);
        }
    }

    /**
     * Получает список товаров в избранном для пользователя.
     * @param userId ID пользователя
     * @return список товаров (может быть пустым)
     */
    //public List<Product> findFavoritesByUser(long userId){}

    /**
     * Добавляет товар в избранное пользователя.
     *
     * @param userId ID пользователя
     * @param productId ID товара
     */
    public void save(long userId, long productId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, productId);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            throw new RuntimeException("Ошибка сохранения избранного товара", error);
        }
    }

    /**
     * Удаляет товар из избранного.
     *
     * @param userId ID пользователя
     * @param productId ID товара
     */
    public void delete(long userId, long productId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, productId);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            throw new RuntimeException("Ошибка удаления избранного товара", error);
        }
    }
}
