package by.tms.ecommerceprojectc41onl.mappers;

import by.tms.ecommerceprojectc41onl.dto.CategoryDto;
import by.tms.ecommerceprojectc41onl.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Преобразователь для объектов {@link Category} и {@link CategoryDto}.
 *
 * @author Ирина Мизгир
 * @date 16.08.2026 15:52
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    /**
     * Преобразует объект категории из бд в DTO.
     *
     * @param category Категории из бд.
     * @return DTO категория.
     */
    CategoryDto toDto(Category category);

}
