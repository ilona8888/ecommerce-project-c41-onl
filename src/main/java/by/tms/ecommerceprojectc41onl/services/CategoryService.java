package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.CategoryDao;
import by.tms.ecommerceprojectc41onl.dto.CategoryDto;
import by.tms.ecommerceprojectc41onl.mappers.CategoryMapper;
import by.tms.ecommerceprojectc41onl.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с категорией товара.
 *
 * @author Ирина Мизгир
 * @date 16.08.2026 15:30
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDao categoryDao;

    private final CategoryMapper categoryMapper;

    /**
     * Получаем список всех категорий.
     *
     * @return Список категорий.
     */
    public List<CategoryDto> findAllCategories() {
        return categoryDao.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public void save(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.name());
        categoryDao.save(category);
    }

}
