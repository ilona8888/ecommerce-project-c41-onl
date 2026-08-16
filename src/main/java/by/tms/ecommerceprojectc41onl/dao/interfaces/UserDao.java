package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> saveUser(User user);
    void deleteUser(User user);
    void update(User user);

    Optional<User> findUserById(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(User user);







}
