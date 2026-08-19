package by.tms.ecommerceprojectc41onl.dto;

/**
 * Данные о файле.
 *
 * @param fileName имя файла
 * @param data данные о файле
 * @author Ирина Мизгир
 * @date 25.07.2026 22:30
 */
public record FileData(String fileName, byte[] data) {
}
