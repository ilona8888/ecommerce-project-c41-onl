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

@Repository
@RequiredArgsConstructor
public class FavouriteDao {

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

    public void save(Long userId, Long productId) {
        addToFavourite(userId, productId);
    }

    public void delete(Long userId, Long productId) {
        removeFromFavourite(userId, productId);
    }
}