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

}

