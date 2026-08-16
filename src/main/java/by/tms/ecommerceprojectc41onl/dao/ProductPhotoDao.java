package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.ProductPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

/**
 * DAO для фото товаров.
 */
@Repository
@RequiredArgsConstructor
public class ProductPhotoDao {

    private final DataSource dataSource;

    private static final String INSERT_QUERY =
            "INSERT INTO PRODUCT_PHOTOS (FILE_ID, PRODUCTS_ID) VALUES (?, ?)";

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
}
