package by.tms.ecommerceprojectc41onl.dto;

/**
 * Dto для объекта создания товара.
 * @param name Имя товара.
 * @param description Описание товара.
 * @param categoryId Идентификатор категории.
 * @param price Стоимость товара.
 * @param fileData Данные файла (фото товара).
 * @param userName Имя пользователя (продавца).
 * @author Ирина Мизгир
 * @date 16.08.2026 18:27
 */
public record CreateProductDto(
        String name,
        String description,
        Integer categoryId,
        double price,
        FileData fileData,
        String userName
) {
}
