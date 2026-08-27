package by.tms.ecommerceprojectc41onl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDTO {

    @NotEmpty(message = "Введите email")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotEmpty(message = "Введите пароль")
    private String password;
}
