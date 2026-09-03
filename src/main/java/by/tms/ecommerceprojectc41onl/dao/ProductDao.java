package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.Category;
import by.tms.ecommerceprojectc41onl.model.Product;
import by.tms.ecommerceprojectc41onl.model.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DAO для работы с товарами.
 *
 * @author Ирина Мизгир
 * @date 16.08.2026 15:31
 */
@Repository
@RequiredArgsConstructor
public class ProductDao {

    private static final String INSERT_QUERY = "INSERT INTO PRODUCTS (NAME, DESCRIPTION, PRICE, SELLERS_ID, CATEGORIES_ID) VALUES (?, ?, ?, ?, ?)";

    // ХАРДКОР: Высчитываем рейтинг на лету для всего каталога
    private static final String FIND_ALL_QUERY = """
            SELECT p.ID, p.NAME, p.PRICE, p.DESCRIPTION, p.SELLERS_ID, p.CATEGORIES_ID,
                   COALESCE(ROUND(AVG(r.RATING)::numeric, 1), 0.0) AS RATING
            FROM PRODUCTS p
            LEFT JOIN REVIEWS r ON p.ID = r.PRODUCTS_ID
            GROUP BY p.ID
            ORDER BY p.ID DESC
            """;

    private final DataSource dataSource;

    /**
     * Создание нового товара.
     */
    public Product create(Product product, Seller seller, Category category) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setBigDecimal(3, product.getPrice());
            preparedStatement.setLong(4, seller.getUser().getId());
            preparedStatement.setLong(5, category.getId());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Не удалось получить сгенерированный id.");
                }
            }
            return product;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения нового товара.", e);
        }
    }

    private Product mapProduct(ResultSet resultSet, String idColumn) throws SQLException {
        Product product = new Product(
                resultSet.getLong(idColumn),
                resultSet.getString("NAME"),
                resultSet.getBigDecimal("PRICE")
        );
        product.setDescription(resultSet.getString("DESCRIPTION"));

        // Подтягиваем динамический рейтинг из SQL-запроса
        product.setRating(resultSet.getDouble("RATING"));

        return product;
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }

        List<Product> products = new ArrayList<>();
        // ХАРДКОР: Рейтинг при поиске
        String sql = """
                SELECT p.ID, p.NAME, p.PRICE, p.DESCRIPTION, p.SELLERS_ID, p.CATEGORIES_ID,
                       COALESCE(ROUND(AVG(r.RATING)::numeric, 1), 0.0) AS RATING
                FROM PRODUCTS p
                LEFT JOIN REVIEWS r ON p.ID = r.PRODUCTS_ID
                WHERE LOWER(p.NAME) LIKE LOWER(?)
                GROUP BY p.ID
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + keyword + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapProduct(resultSet, "ID");
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска товаров", e);
        }

        return products;
    }

    public Product findById(Long id) {
        String sql = """
                SELECT p.ID, p.NAME, p.PRICE, p.DESCRIPTION, p.SELLERS_ID, p.CATEGORIES_ID,
                       COALESCE(ROUND(AVG(r.RATING)::numeric, 1), 0.0) AS RATING
                FROM PRODUCTS p
                LEFT JOIN REVIEWS r ON p.ID = r.PRODUCTS_ID
                WHERE p.ID = ?
                GROUP BY p.ID
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapProduct(resultSet, "ID"); // Используем общий метод маппинга
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска товара по ID", e);
        }
        return null;
    }

    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Product product = mapProduct(resultSet, "ID"); // Используем общий метод маппинга
                products.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }
}