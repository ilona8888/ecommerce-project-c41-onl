package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.Product;
import by.tms.ecommerceprojectc41onl.model.Purchase;
import by.tms.ecommerceprojectc41onl.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PurchaseDao {

    // ХАРДКОР: Считаем средний рейтинг на лету через LEFT JOIN и GROUP BY
    private static final String SELECT_BY_USER_QUERY = """
            SELECT pu.ID, pu.COST, pu.PURCHASE_DATE,
                   p.ID AS PRODUCT_ID, p.NAME, p.PRICE, p.DESCRIPTION,
                   COALESCE(ROUND(AVG(r.RATING)::numeric, 1), 0.0) AS RATING,
                   MAX(pp.FILE_ID) AS IMAGE_ID
            FROM PURCHASES pu
            JOIN PRODUCTS p ON p.ID = pu.PRODUCTS_ID
            LEFT JOIN REVIEWS r ON p.ID = r.PRODUCTS_ID
            LEFT JOIN PRODUCT_PHOTOS pp ON p.ID = pp.PRODUCTS_ID
            WHERE pu.USERS_ID = ?
            GROUP BY pu.ID, pu.COST, pu.PURCHASE_DATE, p.ID, p.NAME, p.PRICE, p.DESCRIPTION
            ORDER BY pu.PURCHASE_DATE DESC
            """;

    // 2. Обновленный запрос одной покупки
    private static final String SELECT_PURCHASED_PRODUCT_QUERY = """
            SELECT p.ID, p.NAME, p.PRICE, p.DESCRIPTION,
                   COALESCE(ROUND(AVG(r.RATING)::numeric, 1), 0.0) AS RATING,
                   MAX(pp.FILE_ID) AS IMAGE_ID
            FROM PURCHASES pu
            JOIN PRODUCTS p ON p.ID = pu.PRODUCTS_ID
            LEFT JOIN REVIEWS r ON p.ID = r.PRODUCTS_ID
            LEFT JOIN PRODUCT_PHOTOS pp ON p.ID = pp.PRODUCTS_ID
            WHERE pu.USERS_ID = ? AND pu.PRODUCTS_ID = ?
            GROUP BY p.ID, p.NAME, p.PRICE, p.DESCRIPTION
            LIMIT 1
            """;

    private final DataSource dataSource;

    public List<Purchase> findByUser(User user) {
        List<Purchase> purchases = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_USER_QUERY)) {
            statement.setLong(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapProduct(resultSet, "PRODUCT_ID");
                    Purchase purchase = new Purchase(resultSet.getBigDecimal("COST"), user, product);
                    purchase.setId(resultSet.getLong("ID"));
                    purchase.setPurchaseDate(resultSet.getTimestamp("PURCHASE_DATE").toLocalDateTime());
                    purchases.add(purchase);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load user purchases", e);
        }

        return purchases;
    }

    public Optional<Product> findPurchasedProduct(Long userId, Long productId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PURCHASED_PRODUCT_QUERY)) {
            statement.setLong(1, userId);
            statement.setLong(2, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        ? Optional.of(mapProduct(resultSet, "ID"))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to verify user purchase", e);
        }
    }

    private Product mapProduct(ResultSet resultSet, String idColumn) throws SQLException {
        Product product = new Product(
                resultSet.getLong(idColumn),
                resultSet.getString("NAME"),
                resultSet.getBigDecimal("PRICE")
        );
        product.setDescription(resultSet.getString("DESCRIPTION"));
        product.setRating(resultSet.getDouble("RATING"));

        // Подтягиваем ID картинки (если есть)
        long imageId = resultSet.getLong("IMAGE_ID");
        if (!resultSet.wasNull()) {
            product.setImageId(imageId);
        }

        return product;
    }

    public void save(Purchase purchase) {
        String sql = "INSERT INTO purchases (users_id, products_id, cost, purchase_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, purchase.getUser().getId());
            statement.setLong(2, purchase.getProduct().getId());
            statement.setBigDecimal(3, purchase.getCost());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(purchase.getPurchaseDate()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения покупки", e);
        }
    }
}