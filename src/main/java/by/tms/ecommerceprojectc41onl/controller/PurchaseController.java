package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.model.Purchase;
import by.tms.ecommerceprojectc41onl.model.User; // Не забудь импортировать User!
import by.tms.ecommerceprojectc41onl.services.PurchaseService;
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

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/buy/{id}")
    public String buyProduct(@PathVariable("id") Long productId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:/login";
        }
        purchaseService.buyProduct(productId, currentUser);

        return "redirect:/purchases";
    }

    @GetMapping("/purchases")
    public String showPurchases(Model model, HttpSession session) {

        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:/login";
        }
        List<Purchase> purchases = purchaseService.getCurrentUserPurchases(currentUser);

        model.addAttribute("purchases", purchases);
        return "purchases";
    }
}