package by.tms.ecommerceprojectc41onl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor // Позволит вызывать new Purchase()
@AllArgsConstructor
public class Purchase {
    private Long id;
    private BigDecimal cost;
    private LocalDateTime purchaseDate;
    private User user;
    private Product product;

    public Purchase(BigDecimal cost, User user, Product product) {
        this.cost = cost;
        this.user = user;
        this.product = product;
    }
}
