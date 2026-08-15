package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;

@Data
public class Review {
    private Long id;
    private int rating;
    private String comment;
    private User user;
    private Product product;
}
