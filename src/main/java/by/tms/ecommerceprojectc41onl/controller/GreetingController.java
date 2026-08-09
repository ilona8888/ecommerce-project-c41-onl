package by.tms.ecommerceprojectc41onl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingController {

    // Главная страница проекта - каталог товаров
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
