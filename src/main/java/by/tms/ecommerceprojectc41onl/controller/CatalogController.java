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
import org.springframework.web.bind.annotation.RequestParam; // Не забудь импорт!

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
            @RequestParam(required = false, name = "categoryId") Long categoryId,
            Model model,
            HttpSession session
    ) {
        // Текущий пользователь для сердечек избранного
        User currentUser = sessionService.getCurrentUser(session);

        // Карточки товаров: если передан categoryId — фильтруем, иначе показываем все
        List<ProductCardDto> cards;
        if (categoryId != null) {
            cards = productService.getProductsByCategories(List.of(categoryId));
        } else {
            cards = productService.getAllProductCards(currentUser);
        }
        model.addAttribute("productCards", cards);

        // Передаем список категорий для выпадающего меню фильтра
        model.addAttribute("allCategories", categoryService.findAllCategories());

        return "index";
    }
}