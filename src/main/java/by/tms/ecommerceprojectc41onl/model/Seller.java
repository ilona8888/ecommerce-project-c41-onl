package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Seller {
    private User user;
    private String details;
    private String contactInfo;
    private List<Product> products = new ArrayList<>();

    public Seller(User user, String details, String contactInfo) {
        this.user = user;
        this.details = details;
        this.contactInfo = contactInfo;
    }

}
