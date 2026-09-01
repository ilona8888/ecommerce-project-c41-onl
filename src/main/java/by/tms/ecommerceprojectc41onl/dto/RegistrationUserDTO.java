package by.tms.ecommerceprojectc41onl.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class RegistrationUserDTO {

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    @Size(min = 2, max = 20, message = "Имя пользователя должно быть от 2 до 20 символов")
    private String username;

    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 16, message = "Имя должно содержать от 2 до 16 букв")
    private String firstname;

    @NotBlank(message = "Фамилия обязательна для заполнения")
    @Size(min = 2, max = 16, message = "Фамилия должна содержать от 2 до 16 букв")
    private String lastname;

    @NotBlank(message = "Пароль обязателен")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*\\(\\),\\.\\?\\\":\\{\\}\\|<>_])[A-Za-z\\d!@#\\$%\\^&\\*\\(\\),\\.\\?\\\":\\{\\}\\|<>_]{8,20}$",
            message = "Невалидный пароль"
    )
    private String password;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат электронной почты")
    @Size(min = 5, max = 50, message = "Email должен быть от 5 до 50 символов")
    private String email;

    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Дата рождения должна быть в прошлом")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;
}

