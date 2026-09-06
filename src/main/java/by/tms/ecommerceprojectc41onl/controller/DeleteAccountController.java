package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dao.UserDao;
import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.DeleteAccountService;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeleteAccountController {

    @Autowired
    DeleteAccountService deleteAccountService;
    @Autowired
    SessionService sessionService;

    @GetMapping("/deleteAccount")
    public String deletePage() {
        return "delete-account";
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(HttpServletRequest req, HttpSession httpSession) {
        User user = sessionService.getCurrentUser(req.getSession());
        if (user == null) {
            return "redirect:/login";
        }
        deleteAccountService.deleteAccount(user);
        httpSession.invalidate();
        return "redirect:/";
    }
}
