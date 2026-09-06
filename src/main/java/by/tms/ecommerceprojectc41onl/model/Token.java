package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Token {
    private UUID id;
    private String type;
    private boolean isActive;
    private User user;
    private LocalDate createdDate;
}
