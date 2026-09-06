package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

/**
 * DAO для работы с файлами.
 */
@Repository
@RequiredArgsConstructor
public class FileDao {

    private static final String INSERT_QUERY = "INSERT INTO FILES (FILE_NAME, DATA) VALUES (?, ?)";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM FILES WHERE id = ?";

    private final DataSource dataSource;

    /**
     * Создание нового файла.
     *
     * @param file Файл.
     * @return Файл (с заполненным идентификатором).
     */
    public File create(File file) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, file.getFileName());
            preparedStatement.setBytes(2, file.getFile());
            preparedStatement.executeUpdate();
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    file.setId(keys.getLong(1));
                } else {
                    throw new SQLException("Не удалось получить сгенерированный id.");
                }
            }
            return file;
        } catch (SQLException error) {
            throw new RuntimeException("Ошибка сохранения файла", error);
        }
    }

    /**
     * Получение файла по идентификатору.
     *
     * @param id Файловый идентификатор.
     * @return Данные о файле из БД.
     */
    public File getById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToFile(resultSet);
                }

                throw new RuntimeException("Не найден файл по id %s".formatted(id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска файла", e);
        }
    }

    /**
     * Преобразование данных из БД в объект файла.
     *
     * @param resultSet Данные результата запроса из БД.
     * @return Данные о файле.
     * @throws SQLException Исключение при доступе к данным из БД.
     */
    private File mapToFile(ResultSet resultSet) throws SQLException {
        var file = new File();
        file.setId(resultSet.getLong("id"));
        file.setFileName(resultSet.getString("file_name"));
        file.setFile(resultSet.getBytes("data"));
        file.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());

        return file;
    }

}

