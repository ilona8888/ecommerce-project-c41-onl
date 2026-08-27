package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.ProductPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

/**
 * DAO для фото товаров.
 */
@Repository
@RequiredArgsConstructor
public class ProductPhotoDao {

    private final DataSource dataSource;

    private static final String INSERT_QUERY =
            "INSERT INTO PRODUCT_PHOTOS (FILE_ID, PRODUCTS_ID) VALUES (?, ?)";

    private static final String FIND_PHOTO_ID_BY_PRODUCT_ID = "SELECT FILE_ID FROM PRODUCT_PHOTOS WHERE PRODUCTS_ID = ?";

    /**
     * Создание фото товара.
     *
     * @param photo фото товара.
     * @return фото товара (с заполненным идентификатором).
     */
    public ProductPhoto create(ProductPhoto photo) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, photo.getFile().getId());
            preparedStatement.setLong(2, photo.getProduct().getId());
            preparedStatement.executeUpdate();
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    photo.setId(keys.getLong(1));
                } else {
                    throw new SQLException("Не удалось получить сгенерированный id.");
                }
            }
            return photo;
        } catch (SQLException error) {
            throw new RuntimeException("Ошибка сохранения фото товара", error);
        }
    }

    public Optional<Long> getPhotoIdByProductId(long productId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_PHOTO_ID_BY_PRODUCT_ID)) {

            ps.setLong(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getLong("FILE_ID"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }
}
