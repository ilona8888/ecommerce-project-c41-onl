package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Favorite {

    private User user;
    private Product product;
    private LocalDateTime dateAdded;

    public Favorite(User user, Product product, LocalDateTime dateAdded)
    {
        this.user = user;
        this.product = product;
        this.dateAdded = dateAdded;
    }
}