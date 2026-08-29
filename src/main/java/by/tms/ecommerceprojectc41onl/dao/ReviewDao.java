package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.Product;
import by.tms.ecommerceprojectc41onl.model.Review;
import by.tms.ecommerceprojectc41onl.model.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReviewDao {

    private static final String SELECT_AVERAGE_RATING_BY_ID = """
            SELECT AVG(rating) AS average_value FROM reviews WHERE products_id = ?""";

    private final DataSource dataSource;

    public ReviewDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addReview(Review review) {
        String sql = """
                INSERT INTO REVIEWS (RATING, COMMENT, USERS_ID, PRODUCTS_ID)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (USERS_ID, PRODUCTS_ID)
                DO UPDATE SET RATING = EXCLUDED.RATING,
                              COMMENT = COALESCE(EXCLUDED.COMMENT, REVIEWS.COMMENT)
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, review.getRating());
            statement.setString(2, normalizeComment(review.getComment()));
            statement.setLong(3, review.getUser().getId());
            statement.setLong(4, review.getProduct().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save review", e);
        }
    }

    public void addComment(Review review) {
        String sql = """
                UPDATE REVIEWS
                SET COMMENT = ?
                WHERE USERS_ID = ? AND PRODUCTS_ID = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, normalizeComment(review.getComment()));
            statement.setLong(2, review.getUser().getId());
            statement.setLong(3, review.getProduct().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save review comment", e);
        }
    }

    public List<Review> findByProductId(Long productId) {
        String sql = """
                SELECT r.ID, r.RATING, r.COMMENT,
                       u.ID AS USER_ID, u.USER_NAME,
                       p.ID AS PRODUCT_ID, p.NAME, p.PRICE
                FROM REVIEWS r
                JOIN USERS u ON u.ID = r.USERS_ID
                JOIN PRODUCTS p ON p.ID = r.PRODUCTS_ID
                WHERE r.PRODUCTS_ID = ?
                ORDER BY r.ID DESC
                """;
        List<Review> reviews = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Review review = new Review();
                    review.setId(resultSet.getLong("ID"));
                    review.setRating(resultSet.getInt("RATING"));
                    review.setComment(resultSet.getString("COMMENT"));

                    User user = new User(resultSet.getLong("USER_ID"), null, null);
                    user.setUserName(resultSet.getString("USER_NAME"));
                    review.setUser(user);

                    Product product = new Product(
                            resultSet.getLong("PRODUCT_ID"),
                            resultSet.getString("NAME"),
                            resultSet.getBigDecimal("PRICE")
                    );
                    review.setProduct(product);
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load product reviews", e);
        }
        return reviews;
    }

    private String normalizeComment(String comment) {
        return comment == null || comment.isBlank() ? null : comment.trim();
    }

    public double getProductRating(Long productId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_AVERAGE_RATING_BY_ID)) {

            preparedStatement.setLong(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("average_value");
                }

                throw new RuntimeException("Не возможно посчитать среднюю оценку по id %s товара".formatted(productId));
            }
        } catch (SQLException error) {
            throw new RuntimeException("Ошибка расчёта средней оценки", error);
        }
    }
}
