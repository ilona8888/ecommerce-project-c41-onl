package by.tms.ecommerceprojectc41onl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Данные запроса для создания товара.
 *
 * @author Ирина Мизгир
 * @date 16.08.2026 16:44
 */
@Data
public class CreateProductRequestDto {

    /**
     * Имя товара.
     */
    private @NotBlank String name;

    /**
     * Описание товара.
     */
    private @NotBlank String description;

    /**
     * Id категории.
     */
    private  @NotNull(message = "Необходимо выбрать категорию товара") Integer categoryId;
    /**
     * Цена товара.
     */
    private @Positive double price;

    /**
     * Файл товара.
     */
    private @NotNull MultipartFile file;

}
