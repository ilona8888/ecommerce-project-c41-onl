package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Token {
    private Long id;
    private String type;
    private boolean isActive;
    private User user;
    private LocalDate createdDate;
}
