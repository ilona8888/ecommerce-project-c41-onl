package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dto.RegistrationUserDTO;
import by.tms.ecommerceprojectc41onl.model.Token;
import by.tms.ecommerceprojectc41onl.services.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new RegistrationUserDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String createPost(@Valid @ModelAttribute("user") RegistrationUserDTO registrationUserDTO,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", registrationUserDTO);
            return "registration";
        }
        try {
            if (!registrationService.isEmailUnique(registrationUserDTO.getEmail())) {
                bindingResult.rejectValue("email", "email.error", "Пользователь с такой почтой уже есть.");
                model.addAttribute("user", registrationUserDTO);
                return "registration";
            }
            if (!registrationService.isUserNameUnique(registrationUserDTO.getUsername())) {
                bindingResult.rejectValue("username", "username.error", "Пользователь с таким именем уже есть.");
                model.addAttribute("user", registrationUserDTO);
                return "registration";
            }
            Token token = registrationService.register(registrationUserDTO);
            model.addAttribute("tokenUuid", token.getId());
            return "registration-success";
        } catch (RuntimeException e) {
            model.addAttribute("globalError", "Произошла непредвиденная ошибка.");
            model.addAttribute("user", registrationUserDTO);
            return "registration";
        }
    }

    @GetMapping("/registration/confirm")
    public String confirm(@RequestParam(value = "token", required = false) String token, Model model) {
        if (token == null || token.isBlank()) {
            model.addAttribute("error", "Ссылка недействительна.");
            return "registration";
        }
        try {
            UUID tokenUuid = UUID.fromString(token);
            boolean isUserActive = registrationService.activateUser(tokenUuid);
            if (isUserActive) {
                model.addAttribute("message", "Регистрация прошла успешно! Теперь можно войти.");
            } else {
                model.addAttribute("error", "Ссылка недействительна или уже использована.");
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Ссылка недействительна.");
        }
        return "redirect:/login";
    }
}
