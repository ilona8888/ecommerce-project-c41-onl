package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class Review {
    private Long id;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    private String comment;
    private User user;
    private Product product;
}
