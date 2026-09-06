package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для работы с категориями товаров.
 *
 * @author Ирина Мизгир
 * @date 16.08.2026 15:31
 */
@Repository
@RequiredArgsConstructor
public class CategoryDao {

    private static final String SELECT_ALL_QUERY = "SELECT * FROM CATEGORIES";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM CATEGORIES WHERE id = ?";

    private static final String INSERT_CATEGORY_QUERY = "INSERT INTO CATEGORIES (NAME) VALUES (?)";

    private final DataSource dataSource;

    /**
     * Получаем список всех категорий.
     *
     * @return Список категорий.
     */
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(mapToCategory(resultSet));
            }

            return categories;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска категорий", e);
        }
    }

    private Category mapToCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getLong("ID"));
        category.setName(resultSet.getString("NAME"));
        return category;
    }

    /**
     * Получение категории по его идентификатору.
     *
     * @param id идентификатор товара
     * @return категория товара
     */
    public Category getById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToCategory(resultSet);
                }

                throw new RuntimeException("Не найдена категория по id %s".formatted(id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска категории", e);
        }
    }

    public void save(Category category) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Не удалось получить сгенерированный id.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения новой категории.", e);
        }
    }
}
