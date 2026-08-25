package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.Purchase;
import by.tms.ecommerceprojectc41onl.model.User;

import java.util.List;

public interface PurchaseDao extends GenericDao<Purchase, Long> {
    void save(Purchase purchase);
    List<Purchase> findByUser(User user);
}
