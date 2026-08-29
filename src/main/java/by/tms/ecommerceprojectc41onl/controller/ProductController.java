package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dao.ProductDao;
import by.tms.ecommerceprojectc41onl.model.Product;
import by.tms.ecommerceprojectc41onl.dto.ProductCardDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {

            // 1. Получаем список товаров из базы
            List<Product> searchResults = productDao.searchProducts(keyword.trim());

            // 2. Превращаем сырые Product в ProductCardDto для красивой отрисовки
            List<ProductCardDto> dtoResults = searchResults.stream()
                    .map(product -> new ProductCardDto(
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getDescription(),
                            null, // Временно передаем null для фото, чтобы проверить саму отрисовку
                            0.0,  // Базовый рейтинг для поиска
                            false // Базовое значение для избранного
                    ))
                    .collect(Collectors.toList());

            // 3. Отдаем в HTML список DTO, а не сырых сущностей!
            model.addAttribute("products", dtoResults);
            model.addAttribute("keyword", keyword);

            if (dtoResults.isEmpty()) {
                model.addAttribute("notFoundMessage", "По данному запросу ничего не найдено");
            }
        } else {
            model.addAttribute("notFoundMessage", "Введите ключевое слово для поиска");
        }

        return "index";
    }
}