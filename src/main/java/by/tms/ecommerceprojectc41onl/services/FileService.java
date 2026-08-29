package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.FileDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Ирина Мизгир
 * @date 29.08.2026 17:39
 */
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileDao fileDao;

    public byte[] getFileById(long fileId) {
        return fileDao.getById(fileId).getFile();
    }
}
