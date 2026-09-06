package by.tms.ecommerceprojectc41onl.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO для карточки товара.
 *
 * @author Ирина Мизгир
 * @date 22.08.2026 23:32
 */
@Data
public class ProductCardDto {

    private final long id;
    private final String name;
    private final BigDecimal price;
    private final String description;
    private final Long photoId;
    private final double score;
    private final boolean favorite;
}
