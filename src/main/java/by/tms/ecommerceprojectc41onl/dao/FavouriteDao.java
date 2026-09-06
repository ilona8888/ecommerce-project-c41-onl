package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DAO для работы с избранным (таблица FAVORITES).
 * Первичный ключ таблицы составной — (USERS_ID, PRODUCTS_ID),
 * поэтому один и тот же товар нельзя добавить в избранное дважды.
 */
@Repository
@RequiredArgsConstructor
public class FavouriteDao {

    /* ON CONFLICT DO NOTHING — повторное нажатие на сердечко не должно падать с ошибкой */
    private static final String INSERT_QUERY = """
            INSERT INTO FAVORITES (USERS_ID, PRODUCTS_ID)
            VALUES (?, ?)
            ON CONFLICT (USERS_ID, PRODUCTS_ID) DO NOTHING
            """;

    private static final String DELETE_QUERY = """
            DELETE FROM FAVORITES
            WHERE USERS_ID = ? AND PRODUCTS_ID = ?
            """;

    private static final String EXISTS_QUERY = """
            SELECT 1
            FROM FAVORITES
            WHERE USERS_ID = ? AND PRODUCTS_ID = ?
            """;

    private static final String FIND_BY_USER_QUERY = """
            SELECT p.ID, p.NAME, p.PRICE, p.DESCRIPTION
            FROM FAVORITES f
            JOIN PRODUCTS p ON p.ID = f.PRODUCTS_ID
            WHERE f.USERS_ID = ?
            ORDER BY f.DATE_ADDED DESC
            """;

    private static final String FIND_PRODUCT_IDS_QUERY = """
            SELECT PRODUCTS_ID
            FROM FAVORITES
            WHERE USERS_ID = ?
            """;

    private final DataSource dataSource;

    /**
     * Добавляет товар в избранное пользователя.
     * Если товар уже в избранном, ничего не делает.
     *
     * @param userId    ID пользователя
     * @param productId ID товара
     * @return количество добавленных строк (0 или 1)
     */
    public int addToFavourite(long userId, long productId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {

            statement.setLong(1, userId);
            statement.setLong(2, productId);

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("Не удалось добавить товар в избранное.", e);
        }
    }

    /**
     * Удаляет товар из избранного пользователя.
     *
     * @param userId    ID пользователя
     * @param productId ID товара
     * @return количество удалённых строк (0 или 1)
     */
    public int removeFromFavourite(long userId, long productId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {

            statement.setLong(1, userId);
            statement.setLong(2, productId);

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("Не удалось удалить товар из избранного.", e);
        }
    }

    /**
     * Проверяет, есть ли товар в избранном у пользователя.
     *
     * @param userId    ID пользователя
     * @param productId ID товара
     * @return true, если товар в избранном, иначе false
     */
    public boolean exists(long userId, long productId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_QUERY)) {

            statement.setLong(1, userId);
            statement.setLong(2, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Не удалось проверить наличие товара в избранном.", e);
        }
    }

    /**
     * Получает список товаров в избранном для пользователя.
     * Свежие записи идут первыми.
     *
     * @param userId ID пользователя
     * @return список товаров (может быть пустым)
     */
    public List<Product> findFavoritesByUser(long userId) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_QUERY)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product(
                            resultSet.getLong("ID"),
                            resultSet.getString("NAME"),
                            resultSet.getBigDecimal("PRICE")
                    );
                    product.setDescription(resultSet.getString("DESCRIPTION"));
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Не удалось загрузить избранное пользователя.", e);
        }

        return products;
    }

    /**
     * Возвращает ID всех товаров в избранном у пользователя.
     * Нужен для каталога: одним запросом получаем, какие сердечки закрасить,
     * вместо отдельного exists() на каждую карточку.
     *
     * @param userId ID пользователя
     * @return множество ID товаров (может быть пустым)
     */
    public Set<Long> findFavouriteProductIds(long userId) {
        Set<Long> productIds = new HashSet<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_PRODUCT_IDS_QUERY)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    productIds.add(resultSet.getLong("PRODUCTS_ID"));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Не удалось загрузить избранное пользователя.", e);
        }

        return productIds;
    }
}
