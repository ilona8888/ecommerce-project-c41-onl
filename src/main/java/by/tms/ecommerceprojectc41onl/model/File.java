package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class File {
    private Long id;
    private String fileName;
    private byte [] file;
    private LocalDateTime createdDate;
}
