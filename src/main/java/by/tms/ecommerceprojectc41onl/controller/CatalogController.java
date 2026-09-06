package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dto.ProductCardDto;
import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.CategoryService;
import by.tms.ecommerceprojectc41onl.services.ProductService;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * Контролер для главной страницы.
 */
@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;
    private final SessionService sessionService;
    private final CategoryService categoryService;

    /**
     * Главная страница проекта - каталог товаров (с поддержкой фильтрации по категориям).
     */
    @GetMapping("/")
    public String home(
            @RequestParam(required = false, name = "categoryId") List<Long> categoryIds,
            Model model,
            HttpSession session
    ) {
        // Получаем текущего пользователя
        User currentUser = sessionService.getCurrentUser(session);

        // Получаем карточки
        List<ProductCardDto> cards;
        if (categoryIds != null && !categoryIds.isEmpty()) {
            cards = productService.getProductsByCategories(categoryIds, currentUser);
        } else {
            cards = productService.getAllProductCards(currentUser);
        }
        model.addAttribute("productCards", cards);

        // Передаем категории для меню и фильтра
        model.addAttribute("allCategories", categoryService.findAllCategories());

        return "index";
    }
}