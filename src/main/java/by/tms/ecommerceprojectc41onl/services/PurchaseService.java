package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.ProductDao;
import by.tms.ecommerceprojectc41onl.dao.UserDao;
import by.tms.ecommerceprojectc41onl.dao.interfaces.PurchaseDao;
import by.tms.ecommerceprojectc41onl.model.Purchase;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseDao purchaseDao;
    private final ProductDao productDao;
    private final UserDao userDao;

    public PurchaseService(PurchaseDao purchaseDao, ProductDao productDao, UserDao userDao) {
        this.purchaseDao = purchaseDao;
        this.productDao = productDao;
        this.userDao = userDao;
    }

    // Покупка товара (берем дефолтного пользователя с id = 1)
    public void buyProduct(Long productId) {
        Purchase purchase = new Purchase();

        // Находим товар и пользователя, сразу передаем в покупку
        purchase.setProduct(productDao.findById(productId).orElseThrow(() -> new RuntimeException("Товар не найден")));
        purchase.setUser(userDao.findById(1L).orElseThrow(() -> new RuntimeException("Пользователь не найден")));

        // Сохраняем через DAO
        purchaseDao.save(purchase);
    }

    // Получение списка покупок пользователя с id = 1
    public List<Purchase> getUserPurchases() {
        return purchaseDao.findAllByUser_Id(1L);
    }
}