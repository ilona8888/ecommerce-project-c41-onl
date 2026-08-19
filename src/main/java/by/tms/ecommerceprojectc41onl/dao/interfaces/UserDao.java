package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.model.UserRole;

import java.util.Optional;

public interface UserDao extends GenericDao<User,Long>{

    void deleteById(long id);
    void updateRole(long id, UserRole userRole);
    void updateStatus(long id, boolean active);
    void updatePasswordHash(long id, String newPasswordHash);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
