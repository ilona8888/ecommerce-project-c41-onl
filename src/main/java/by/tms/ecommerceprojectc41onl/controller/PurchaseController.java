package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.model.Purchase;
import by.tms.ecommerceprojectc41onl.services.PurchaseService;
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
    public String buyProduct(@PathVariable("id") Long productId) {
        purchaseService.buyProduct(productId);
        return "redirect:/purchases";
    }

    @GetMapping("/purchases")
    public String showPurchases(Model model) {
        List<Purchase> purchases = purchaseService.getUserPurchases();
        model.addAttribute("purchases", purchases);
        return "purchases"; // возвращает шаблон purchases.html
    }
}