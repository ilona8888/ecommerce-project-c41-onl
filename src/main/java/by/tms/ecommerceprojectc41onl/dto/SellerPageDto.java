/**
 * Classname    SellerPageDto
 * @version     0.01
 * @author      Aleksei Borzetsov
 * date         23.08.2026
 */

package by.tms.ecommerceprojectc41onl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SellerPageDto {

    @NotEmpty(message = "Поле не должно быть пустым")
    @NotBlank(message = "Поле не должно состоять из пробелов")
    @Size(min = 3, max = 16, message = "Размер поля должен быть в диапазоне от 3 до 16 символов")
    private String legalEntity;

    @NotEmpty(message = "Поле не должно быть пустым")
    @NotBlank(message = "Поле не должно состоять из пробелов")
    @Pattern(regexp = "^\\+\\d{11}$",message = "Поле должно содержать 11 цифр и начинаться с '+'")
    private String contactInfo;
}
