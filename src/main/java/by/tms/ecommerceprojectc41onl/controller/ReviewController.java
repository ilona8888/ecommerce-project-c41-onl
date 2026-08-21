package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dao.PurchaseDao;
import by.tms.ecommerceprojectc41onl.dao.ReviewDao;
import by.tms.ecommerceprojectc41onl.model.Product;
import by.tms.ecommerceprojectc41onl.model.Review;
import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.ReviewCurrentUserProvider;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ReviewController {

    private final ReviewDao reviewDao;
    private final PurchaseDao purchaseDao;
    private final ReviewCurrentUserProvider currentUserProvider;

    public ReviewController(ReviewDao reviewDao,
                            PurchaseDao purchaseDao,
                            ReviewCurrentUserProvider currentUserProvider) {
        this.reviewDao = reviewDao;
        this.purchaseDao = purchaseDao;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/purchases")
    public String purchases(Model model) {
        User user = currentUserProvider.getCurrentUser();
        model.addAttribute("purchases",
                user == null ? List.of() : purchaseDao.findByUser(user));
        return "purchases";
    }

    @GetMapping("/product")
    public String productDetails(@RequestParam(value = "productId", required = false) Long productId,
                                 Model model) {
        model.addAttribute("reviews",
                productId == null ? List.of() : reviewDao.findByProductId(productId));
        return "product-details";
    }

    @PostMapping("/reviews")
    public String addReview(@Valid @ModelAttribute("review") Review review,
                            BindingResult bindingResult,
                            @RequestParam("productId") Long productId,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("reviewError", "Выберите оценку от 1 до 5.");
            return "redirect:/purchases";
        }

        User user = currentUserProvider.getCurrentUser();
        Product product = findPurchasedProduct(user, productId);
        if (user == null || product == null) {
            redirectAttributes.addFlashAttribute("reviewError", "Купленный товар не найден.");
            return "redirect:/purchases";
        }

        review.setUser(user);
        review.setProduct(product);
        reviewDao.addReview(review);
        redirectAttributes.addFlashAttribute("reviewSuccess", "Оценка сохранена.");
        return "redirect:/purchases";
    }

    @PostMapping("/reviews/comment")
    public String addComment(@RequestParam("productId") Long productId,
                             @RequestParam("comment") String comment,
                             RedirectAttributes redirectAttributes) {
        if (comment.isBlank()) {
            redirectAttributes.addFlashAttribute("reviewError", "Введите комментарий.");
            return "redirect:/purchases";
        }

        User user = currentUserProvider.getCurrentUser();
        Product product = findPurchasedProduct(user, productId);
        if (user == null || product == null) {
            redirectAttributes.addFlashAttribute("reviewError", "Купленный товар не найден.");
            return "redirect:/purchases";
        }

        boolean reviewExists = reviewDao.findByProductId(productId).stream()
                .anyMatch(review -> review.getUser() != null
                        && user.getId().equals(review.getUser().getId()));
        if (!reviewExists) {
            redirectAttributes.addFlashAttribute("reviewError", "Сначала поставьте оценку.");
            return "redirect:/purchases";
        }

        Review review = new Review();
        review.setComment(comment.trim());
        review.setUser(user);
        review.setProduct(product);
        reviewDao.addComment(review);
        redirectAttributes.addFlashAttribute("reviewSuccess", "Комментарий сохранён.");
        return "redirect:/purchases";
    }

    private Product findPurchasedProduct(User user, Long productId) {
        if (user == null || user.getId() == null || productId == null) {
            return null;
        }
        return purchaseDao.findPurchasedProduct(user.getId(), productId).orElse(null);
    }
}
