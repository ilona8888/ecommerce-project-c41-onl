package by.tms.ecommerceprojectc41onl.controller;


import by.tms.ecommerceprojectc41onl.dao.ProductDao;
import by.tms.ecommerceprojectc41onl.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Product> searchResults = productDao.searchProducts(keyword.trim());

            model.addAttribute("products", searchResults);
            model.addAttribute("keyword", keyword);

            if (searchResults.isEmpty()) {
                model.addAttribute("notFoundMessage", "По данному запросу ничего не найдено");
            }
        } else {
            // Если передан пустой запрос — можно вернуть пустой список или сообщение
            model.addAttribute("notFoundMessage", "Введите ключевое слово для поиска");
        }

        return "index";
    }
}
