package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.SellerDao;
import by.tms.ecommerceprojectc41onl.dao.UserDao;
import by.tms.ecommerceprojectc41onl.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final UserDao userDao;
    private final SellerDao sellerDao;

    public void changeRoleToSeller(long userId) {
        // 1. Меняем роль в таблице USERS с BUYER на SELLER
        userDao.updateRole(userId, UserRole.SELLER);

        try {
            sellerDao.createSeller(userId);
        } catch (Exception e) {
        }
    }
}