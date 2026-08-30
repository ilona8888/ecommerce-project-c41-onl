package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dao.FavouriteDao;
import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.ProductService;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер избранного: страница со списком и добавление/удаление товара.
 * Гостей на эти пути не пускает AuthFilter, но проверку на null оставляем —
 * контроллер не должен зависеть от того, что фильтр где-то настроен.
 */
@Controller
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavouriteController {

    private static final String FAVORITES_PAGE = "/favorites";

    private final FavouriteDao favouriteDao;

    private final ProductService productService;

    private final SessionService sessionService;

    /**
     * Страница «Избранное» — карточки товаров, добавленных пользователем.
     */
    @GetMapping
    public String favorites(Model model, HttpSession session) {

        User currentUser = sessionService.getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("productCards", productService.getFavouriteProductCards(currentUser));

        return "favorites";
    }

    /**
     * Добавляет товар в избранное и переводит пользователя на страницу избранного.
     *
     * @param productId ID товара
     */
    @PostMapping("/add/{productId}")
    public String addToFavourite(@PathVariable("productId") Long productId, HttpSession session) {

        User currentUser = sessionService.getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }

        favouriteDao.addToFavourite(currentUser.getId(), productId);

        return "redirect:" + FAVORITES_PAGE;
    }

    /**
     * Удаляет товар из избранного и возвращает пользователя на ту страницу,
     * с которой он нажал на сердечко (каталог или само избранное).
     *
     * @param productId ID товара
     * @param returnUrl адрес страницы, с которой пришёл запрос
     */
    @PostMapping("/remove/{productId}")
    public String removeFromFavourite(@PathVariable("productId") Long productId,
                                      @RequestParam(value = "returnUrl", required = false) String returnUrl,
                                      HttpSession session) {

        User currentUser = sessionService.getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }

        favouriteDao.removeFromFavourite(currentUser.getId(), productId);

        return "redirect:" + safeReturnUrl(returnUrl);
    }

    /**
     * Возвращаем пользователя только на внутренние страницы приложения,
     * чтобы через параметр формы нельзя было увести его на чужой сайт.
     */
    private String safeReturnUrl(String returnUrl) {
        boolean internal = returnUrl != null
                && returnUrl.startsWith("/")
                && !returnUrl.startsWith("//");

        return internal ? returnUrl : FAVORITES_PAGE;
    }
}
