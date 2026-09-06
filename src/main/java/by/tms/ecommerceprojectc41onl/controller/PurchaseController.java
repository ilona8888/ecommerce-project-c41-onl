package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.model.Purchase;
import by.tms.ecommerceprojectc41onl.model.Review;
import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.PurchaseService;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final SessionService sessionService;


    public PurchaseController(PurchaseService purchaseService, SessionService sessionService) {
        this.purchaseService = purchaseService;
        this.sessionService = sessionService;
    }

    //
    @PostMapping("/buy/{id}")
    public String buyProduct(@PathVariable("id") Long productId, HttpSession session, Model model) {
        User currentUser = sessionService.getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }
        purchaseService.buyProduct(productId, currentUser);

        return "redirect:/purchases";
    }

    @GetMapping("/purchases")
    public String purchases(Model model, HttpSession session) {
        User user = sessionService.getCurrentUser(session);

        List<Purchase> purchasesList = (user == null) ? List.of() : purchaseService.getCurrentUserPurchases(user);

        model.addAttribute("purchases", purchasesList);
        model.addAttribute("review", new Review());

        return "purchases";
    }
}