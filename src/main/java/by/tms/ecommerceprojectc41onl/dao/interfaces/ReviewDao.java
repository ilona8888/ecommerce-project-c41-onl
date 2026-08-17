package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.Review;

import java.util.List;

public interface ReviewDao extends GenericDao<Review, Long> {

    List<Review> findByProduct(long productId);

    double averageRating(long productId);
}
