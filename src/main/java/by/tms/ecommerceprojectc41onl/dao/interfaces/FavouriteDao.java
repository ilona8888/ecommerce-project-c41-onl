package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.Favorite;
import by.tms.ecommerceprojectc41onl.model.Product;

import java.util.List;

public interface FavouriteDao extends GenericDao<Favorite, Long> {

    void removeProduct(long userId, long productId);

    boolean exists(long userId, long productId);

    List<Product> findFavoritesByUser(long userId);
}
