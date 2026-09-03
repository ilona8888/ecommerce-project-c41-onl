package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dao.ProductDao;
import by.tms.ecommerceprojectc41onl.dao.PurchaseDao;
import by.tms.ecommerceprojectc41onl.dao.ReviewDao;
import by.tms.ecommerceprojectc41onl.model.Product;
import by.tms.ecommerceprojectc41onl.model.Review;
import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewDao reviewDao;
    private final PurchaseDao purchaseDao;
    private final ProductDao productDao; // Добавили поле
    private final SessionService sessionService;

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
                            RedirectAttributes redirectAttributes,
                            HttpSession session) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("reviewError", "Выберите оценку от 1 до 5.");
            return "redirect:/purchases";
        }

        User user = sessionService.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        Product product = findPurchasedProduct(user, productId);
        if (product == null) {
            redirectAttributes.addFlashAttribute("reviewError", "Купленный товар не найден.");
            return "redirect:/purchases";
        }

        // 1. Сохраняем новую оценку
        review.setUser(user);
        review.setProduct(product);
        reviewDao.addReview(review);

        // 2. ПЕРЕСЧЕТ РЕЙТИНГА ТОВАРА
        Double newAverageRating = reviewDao.getAverageRatingByProductId(productId);
        productDao.updateProductRating(productId, newAverageRating);

        redirectAttributes.addFlashAttribute("reviewSuccess", "Оценка сохранена, рейтинг товара обновлен.");
        return "redirect:/purchases";
    }

    @PostMapping("/reviews/comment")
    public String addComment(@RequestParam("productId") Long productId,
                             @RequestParam("comment") String comment,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        if (comment.isBlank()) {
            redirectAttributes.addFlashAttribute("reviewError", "Введите комментарий.");
            return "redirect:/purchases";
        }

        User user = sessionService.getCurrentUser(session);
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