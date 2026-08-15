package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductPhoto {
    private Long id;
    private LocalDateTime createdDate;
    private File file;
    private Product product;
}
