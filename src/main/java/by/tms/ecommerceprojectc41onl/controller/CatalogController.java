package by.tms.ecommerceprojectc41onl.controller;


import by.tms.ecommerceprojectc41onl.dto.ProductCardDto;
import by.tms.ecommerceprojectc41onl.services.CategoryService;
import by.tms.ecommerceprojectc41onl.services.ProductService;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


//TODO : ПРОБЛЕМА : невозможно отобразить карточки товара на главной странице
@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;


    // Главная страница проекта - каталог товаров
    @GetMapping("/")
    public String home(Model model) {

        // товары // TODO : доработать productService.getAllProductCards()
        List<ProductCardDto> cards = productService.getAllProductCards();
        model.addAttribute("productCards", cards);
        return "index";
    }
}
