package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.PurchaseService;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

 // TODO : purchaseService не может зарегистрироваться как бин
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
    public String buyProduct(@PathVariable("id") Long productId, HttpSession session) {
        User currentUser = sessionService.getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }
        purchaseService.buyProduct(productId, currentUser);

        return "redirect:/purchases";
    }

    // TODO : такой метод находится ReviewController(строчка 47), надо доработать, чтобы также выводилась карточка товара(можно сюда перенести метод и доработать)
//    @GetMapping("/purchases")
//    public String showPurchases(Model model, HttpSession session) {
//
//        User currentUser = sessionService.getCurrentUser(session);
//
//        if (currentUser == null) {
//            return "redirect:/login";
//        }
//        List<Purchase> purchases = purchaseService.getCurrentUserPurchases(currentUser);
//
//        model.addAttribute("purchases", purchases);
//        return "purchases";
//    }
}