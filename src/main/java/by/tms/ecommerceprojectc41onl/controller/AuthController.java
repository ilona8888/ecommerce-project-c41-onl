package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dto.LoginDTO;
import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.AuthService;
import by.tms.ecommerceprojectc41onl.services.CategoryService;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.AuthenticationException;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final SessionService sessionService;
    private final AuthService authService;
    private final CategoryService categoryService;


    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDto", new LoginDTO());
        model.addAttribute("categories", categoryService.findAllCategories());
        return "login";
    }

    @PostMapping("/login") //POST localhost:8080/login?login=<val>&password=<val>&rememberMe=<value>
    public String login(@Valid @ModelAttribute("loginDto") LoginDTO loginDTO,
                        BindingResult result,
                        @RequestParam(value = "rememberMe", required = false) String rememberMe,
                        HttpServletRequest req,
                        HttpServletResponse resp,
                        Model model) {

        if (result.hasErrors()) {
            return "login";
        }
        try {
            User user = authService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());
            sessionService.setUser(req.getSession(), user);

            if ("true".equals(rememberMe) || "on".equals(rememberMe)) {
                sessionService.rememberMe(resp, user.getId());
            }

            return "redirect:/";
        } catch (AuthenticationException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }


    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse resp) {
        sessionService.logout(req, resp);
        return "redirect:/login";
    }

}

