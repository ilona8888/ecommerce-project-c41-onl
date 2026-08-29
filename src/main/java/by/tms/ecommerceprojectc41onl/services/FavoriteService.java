package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.FavouriteDao;
import by.tms.ecommerceprojectc41onl.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Ирина Мизгир
 * @date 29.08.2026 20:01
 */
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavouriteDao favouriteDao;

    public void markAsFavorite(User user, boolean flag, long productId) {
        if (flag) {
            favouriteDao.save(user.getId(), productId);
        } else {
            favouriteDao.delete(user.getId(), productId);
        }
    }
}
