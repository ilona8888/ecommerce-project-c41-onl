package by.tms.ecommerceprojectc41onl.controller;


import by.tms.ecommerceprojectc41onl.dto.ProductCardDto;
import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.ProductService;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;

    private final SessionService sessionService;

    // Главная страница проекта - каталог товаров
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User currentUser = sessionService.getCurrentUser(session);
        List<ProductCardDto> cards = productService.getAllProductCards(currentUser);
        model.addAttribute("productCards", cards);

        return "index";
    }
}
