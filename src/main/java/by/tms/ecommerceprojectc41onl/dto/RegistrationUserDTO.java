package by.tms.ecommerceprojectc41onl.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class RegistrationUserDTO {
@NotBlank
@NotEmpty
@Size(min = 8, max = 16)
 private String name;
 @NotBlank
 @NotEmpty
 @Size(min = 8, max = 16)
 private String lastname;
 @NotBlank(message = "Password обязателен")
 @Pattern(regexp = "[a-zA-Z0-9@#%&*]{8,20},message = \"Эмодзи не разрешены\"")
 private String password;
 @Size(min = 8, max = 20)
 @NotBlank(message = "Email обязателен")
 private String email;
 @Pattern(regexp = "^[0-9+]{7,15}$", message = "Некорректный формат телефона")
 private long phone;
}

