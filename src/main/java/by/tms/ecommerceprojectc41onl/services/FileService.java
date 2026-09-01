package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.FileDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с файлами.
 * @author Ирина Мизгир
 * @date 29.08.2026 17:39
 */
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileDao fileDao;

    /**
     * Получение файла по идентификатору.
     * @param fileId Файловый идентификатор.
     * @return Данные файла.
     */
    public byte[] getFileById(long fileId) {
        return fileDao.getById(fileId).getFile();
    }
}
