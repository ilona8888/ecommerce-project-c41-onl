package by.tms.ecommerceprojectc41onl.controller;


import by.tms.ecommerceprojectc41onl.dto.CategoryDto;
import by.tms.ecommerceprojectc41onl.dto.ProductCardDto;
import by.tms.ecommerceprojectc41onl.model.Category;
import by.tms.ecommerceprojectc41onl.services.CategoryService;
import by.tms.ecommerceprojectc41onl.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;
    private final CategoryService categoryService;


    // Главная страница проекта - каталог товаров
    @GetMapping("/")
    public String home(@RequestParam(required = false, name = "categoryId") List<Long> categoryIds,
                       Model model) {

        List<ProductCardDto> cards;

        // Если категории выбраны - фильтруем, иначе отдаем все
        if (categoryIds != null && !categoryIds.isEmpty()) {
            cards = productService.getProductsByCategories(categoryIds);
        } else {
            cards = productService.getAllProductCards();
        }

        // Получаем все категории, чтобы отрисовать чекбоксы в фильтре
        List<CategoryDto> allCategories = categoryService.findAllCategories();

        model.addAttribute("productCards", cards);
        model.addAttribute("allCategories", allCategories);
        model.addAttribute("selectedCategoryIds", categoryIds); // Чтобы чекбоксы оставались "нажатыми" после перезагрузки

        return "index";
    }
}
