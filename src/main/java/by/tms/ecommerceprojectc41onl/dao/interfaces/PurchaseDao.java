package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.Purchase;
import by.tms.ecommerceprojectc41onl.model.User;

import java.util.List;

public interface PurchaseDao {

    void save(Purchase purchase);

    List<Purchase> findAll();

    Purchase findById(Long id);

    List<Purchase> findByUser(User user);
}