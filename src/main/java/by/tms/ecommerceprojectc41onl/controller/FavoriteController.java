package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.FavoriteService;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контролер для избранных товаров.
  * @author Ирина Мизгир
 * @date 29.08.2026 19:32
 */
@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final SessionService sessionService;

    private final FavoriteService favoriteService;

    /**
     * Добавление/удаление в/из избранного.
     * @param session Сессия пользователя.
     * @param productId Идентификатор продукта.
     * @param flag Значение избранности (true - добавить в избранное, false- удалить из избранного).
     * @return Страница сайта.
     */
    @PostMapping(value = "/favorite")
    public String makeFavorite(HttpSession session,
                               @RequestParam("product-id") long productId,
                               @RequestParam("flag") boolean flag
    ) {

        User user = sessionService.getCurrentUser(session);
        if(user == null) {
            return "redirect:/login";
        }
        favoriteService.markAsFavorite(user,flag,productId);
        return "redirect:/";
    }

}
