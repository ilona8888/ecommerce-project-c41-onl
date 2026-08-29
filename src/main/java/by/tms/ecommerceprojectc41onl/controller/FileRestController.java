package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ирина Мизгир
 * @date 29.08.2026 17:32
 */
@RestController
@RequiredArgsConstructor
public class FileRestController {

    private final FileService fileService;

    @GetMapping(path = "/files/{fileId}")
    public byte[] getFileById(@PathVariable("fileId") long fileId) {
      return fileService.getFileById(fileId);
    }
}
