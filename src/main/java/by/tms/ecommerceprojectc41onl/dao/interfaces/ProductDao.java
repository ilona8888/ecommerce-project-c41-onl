package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends GenericDao<Product, Long>
{
    List<Product> searchProducts(String keyword);
}
