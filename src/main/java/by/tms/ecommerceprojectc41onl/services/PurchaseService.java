package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.ProductDao;
import by.tms.ecommerceprojectc41onl.dao.PurchaseDao;
import by.tms.ecommerceprojectc41onl.model.Purchase;
import by.tms.ecommerceprojectc41onl.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseDao purchaseDao;
    private final ProductDao productDao;


    public PurchaseService(PurchaseDao purchaseDao, ProductDao productDao) {
        this.purchaseDao = purchaseDao;
        this.productDao = productDao;
    }

    public void buyProduct(Long productId, User user) {
       // Product product = productDao.findById(productId); TODO : Реализовать метод в классе ProductDao

        // TODO : Раскомментировать когда будет product (строчка 28)
//        if (product == null) {
//            throw new IllegalArgumentException("Товар с ID " + productId + " не найден");
//        }
//
//        Purchase purchase = new Purchase();
//        purchase.setUser(user);
//        purchase.setProduct(product);
//        purchase.setPurchaseDate(LocalDateTime.now());

        // purchaseDao.save(purchase); TODO : Реализовать метод в классе ProductDao PurchaseDao
    }

    public List<Purchase> getCurrentUserPurchases(User user) {
        return purchaseDao.findByUser(user);
    }
}