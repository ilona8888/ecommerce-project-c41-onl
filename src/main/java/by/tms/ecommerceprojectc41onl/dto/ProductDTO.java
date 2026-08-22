package by.tms.ecommerceprojectc41onl.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
 @NotBlank
@NotEmpty
 @NotNull
 private String ProductName;
 @Pattern(regexp ="(1-9),{1,6}")
 private BigDecimal price;
 @NotBlank
 @NotEmpty
 @NotNull
 @Size(min = 5, max = 20)
 private String description;
 @NotBlank
 @NotEmpty
 @NotNull
private String  SelectCategory;

}
