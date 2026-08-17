package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.Seller;

import java.util.Optional;

public interface SellerDao extends GenericDao<Seller, Long> {

    void update(Seller seller);

    Optional<Seller> findByUserId(long userId);
}
