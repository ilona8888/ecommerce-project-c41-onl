package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private BigDecimal price;
    private String description;

    public Product(Long id, String name, BigDecimal price)
    {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
