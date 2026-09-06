package by.tms.ecommerceprojectc41onl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для категории.
 *
 * @param id Идентификатор категории.
 * @param name Наименование категории.
 * @author Ирина Мизгир
 * @date 16.08.2026 15:46
 */
public record CategoryDto(Long id,
                          @NotBlank(message = "Название категории обязательно")
                          @Size(
                                  min = 3,
                                  max = 50,
                                  message = "Название должно быть от 3 до 50 символов")
                          String name) {
}
