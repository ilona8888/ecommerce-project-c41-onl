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
import java.util.stream.Collectors;

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

    private final DataSource dataSource;

    /**
     * Создание нового товара.
     *
     * @param product Продукт
     * @param seller Продавец
     * @param category Категория товара
     * @return Продукт (с заполненным идентификатором).
     */
    public Product create(Product product, Seller seller, Category category) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setBigDecimal(3, product.getPrice());
            preparedStatement.setLong(4, seller.getId());
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
    private final List<Product> productsList = new ArrayList<>();

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }

        String lowerKeyword = keyword.toLowerCase();
        return productsList.stream()
                .filter(product -> product.getName() != null &&
                        product.getName().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
    public Product findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getLong("id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getBigDecimal("price"));
                    // Заполните остальные поля товара из БД
                    return product;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска товара по ID", e);
        }
        return null;
    }
}
