package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.Purchase;
import by.tms.ecommerceprojectc41onl.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDao extends GenericDao<Purchase, Long> {
    void save(Purchase purchase);
    List<Purchase> findByUser(User user);
}
