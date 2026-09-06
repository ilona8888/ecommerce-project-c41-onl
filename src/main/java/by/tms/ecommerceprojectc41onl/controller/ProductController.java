package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dao.ProductDao;
import by.tms.ecommerceprojectc41onl.model.Product;
import by.tms.ecommerceprojectc41onl.dto.ProductCardDto;
import by.tms.ecommerceprojectc41onl.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    private final ProductDao productDao;
    private final ProductService productService;

    public ProductController(ProductDao productDao, ProductService productService) {
        this.productDao = productDao;
        this.productService = productService;
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {

            // 1. Получаем список товаров из базы
            List<Product> searchResults = productDao.searchProducts(keyword.trim());

            // 2. Превращаем Product в ProductCardDto через сервис
            List<ProductCardDto> dtoResults = searchResults.stream()
                    .map(product -> productService.toCard(product, Set.of()))
                    .toList();

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