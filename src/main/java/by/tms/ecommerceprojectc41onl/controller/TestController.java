package by.tms.ecommerceprojectc41onl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Временный контроллер для проверки каркаса приложения.
 */
@RequestMapping
@Controller
public class TestController {

    // Главная страница проекта - каталог товаров
    @GetMapping("/test")
    public String home() {
        return "index";
    }

    // Главная страница - каталог товаров
    @GetMapping("/catalog")
    public String catalog() {
        return "index";
    }
    
//    @GetMapping("/favorites")
//    public String favorites() {
//        return "favorites";
//    }

    // Покупки
    @GetMapping("/test/purchases")
    public String purchases() {
        return "purchases";
    }

    // Профиль
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    // Логин
    @GetMapping("/test-login")
    public String login() {
        return "login";
    }

    // Регистрация
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    // Восстановление пароля
    @GetMapping("/password-recovery")
    public String passwordRecovery() {
        return "password-recovery";
    }

    // Ошибка 404
    @GetMapping("/error/404")
    public String error404() {
        return "error/error-404";
    }

    // Ошибка 500
    @GetMapping("/error/500")
    public String error500() {
        return "error/error-500";
    }

    // Ошибка 500
    @GetMapping("/checkError")
    public String check() {
        throw new RuntimeException("Тестовое исключение для проверки 500 ошибки");
    }
}