package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dao.ProductDao;
import by.tms.ecommerceprojectc41onl.dao.ReviewDao;
import by.tms.ecommerceprojectc41onl.model.Product;
import by.tms.ecommerceprojectc41onl.model.Review;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ReviewController {

    private final ReviewDao reviewDao;
    private final ProductDao productDao;
    private final SessionService sessionService;

    public ReviewController(ReviewDao reviewDao,
                            ProductDao productDao,
                            SessionService sessionService) {
        this.reviewDao = reviewDao;
        this.productDao = productDao;
        this.sessionService = sessionService;
    }

    @GetMapping("/product")
    public String productDetails(@RequestParam("productId") Long productId,
                                 Model model) {
        // Загружаем товар
        Product product = productDao.findById(productId);
        model.addAttribute("product", product);

        // Загружаем отзывы (если нужно)
        List<Review> reviews = reviewDao.findByProductId(productId);
        model.addAttribute("reviews", reviews);

        return "product-details";
    }
}