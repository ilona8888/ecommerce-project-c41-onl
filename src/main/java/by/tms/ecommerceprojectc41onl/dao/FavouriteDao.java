package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.model.Product;

import java.util.List;

//TODO : Предполагаю, что будут такие методы(можно что-то убрать или добавить)
public class FavouriteDao {


    /**
     * Удаляет товар из избранного пользователя.
     * @param userId ID пользователя
     * @param productId ID товара
     * @return количество удалённых строк (0 или 1)
     */
   // public int removeProduct(long userId, long productId){}

    /**
     * Проверяет, есть ли товар в избранном у пользователя.
     * @param userId ID пользователя
     * @param productId ID товара
     * @return true, если товар в избранном, иначе false
     */

    //public boolean exists(long userId, long productId){}

    /**
     * Получает список товаров в избранном для пользователя.
     * @param userId ID пользователя
     * @return список товаров (может быть пустым)
     */
    //public List<Product> findFavoritesByUser(long userId){}

    /**
     * Добавляет товар в избранное пользователя.
     * @param userId ID пользователя
     * @param productId ID товара
     */
    //public void save(long userId, long productId){}
}
