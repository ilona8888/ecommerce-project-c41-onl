package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.services.RecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class RecoveryController {

    @Autowired
    private RecoveryService recoveryService;

    @GetMapping("/recovery")
    public String recoveryGet() {
        return "password-recovery";
    }

    @PostMapping("/recovery")
    public String recoveryPost(@RequestParam("email") String email, Model model) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();
        String resetLink = recoveryService.createRecoveryLink(email, baseUrl);
        if (resetLink != null) {
            model.addAttribute("resetLink", resetLink);
        }
        return "recovery-link";
    }

    @GetMapping("/recovery/reset")
    public String recoveryResetGet(@RequestParam("token") String token, Model model) {
        if (!recoveryService.validateToken(token)) {
            model.addAttribute("error", "Ссылка недействительна или её срок истек.");
            return "recovery-reset";
        }
        model.addAttribute("token", token);
        return "recovery-reset";
    }

    @PostMapping("/recovery/reset")
    public String recoveryResetPost(@RequestParam("token") String token,
                                    @RequestParam("password") String password,
                                    Model model) {
        if (!recoveryService.validateToken(token)) {
            model.addAttribute("error", "Ссылка недействительна.");
            return "recovery-reset";
        }
        boolean isResetSuccessful = recoveryService.resetPassword(token, password);
        if (isResetSuccessful) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Не удалось обновить пароль.");
            model.addAttribute("token", token);
            return "recovery-reset";
        }
    }
}
