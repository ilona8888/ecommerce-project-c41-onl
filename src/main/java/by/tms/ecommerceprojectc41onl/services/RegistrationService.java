package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.TokenDao;
import by.tms.ecommerceprojectc41onl.dao.UserDao;
import by.tms.ecommerceprojectc41onl.dto.RegistrationUserDTO;
import by.tms.ecommerceprojectc41onl.model.Token;
import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isEmailUnique(String email) {
        return userDao.findByEmail(email).isEmpty();
    }

    public boolean isUserNameUnique(String username) {
        try {
            userDao.getByName(username);
            return false;
        } catch (RuntimeException e) {
            return true;
        }
    }

    public User createUser(RegistrationUserDTO registrationUserDTO) {
        User user = new User(registrationUserDTO.getUsername(),
                registrationUserDTO.getEmail(),
                registrationUserDTO.getFirstname(),
                registrationUserDTO.getLastname(),
                registrationUserDTO.getBirthday());
        user.setRole(UserRole.BUYER);
        user.setPasswordHash(passwordEncoder.encode(registrationUserDTO.getPassword()));
        user.setStatus(false);
        return userDao.save(user);
    }

    public Token createToken(User user) {
        UUID uuidToken = UUID.randomUUID();
        Token token = new Token();
        token.setId(uuidToken); // хочу uuid оставить
        token.setType("registration");
        token.setActive(true);
        token.setUser(user);
        token.setCreatedDate(LocalDate.now());
        return tokenDao.save(token);
    }

    public Token register(RegistrationUserDTO registrationUserDTO) {
        User savedUser = createUser(registrationUserDTO);
        return createToken(savedUser);
    }

    public boolean activateUser(UUID tokenId) {
        Optional<Token> tokenOptional = tokenDao.findById(tokenId);
        if (tokenOptional.isEmpty() || !tokenOptional.get().isActive()) {
            return false;
        }
        Token token = tokenOptional.get();
        Optional<User> userOptional = userDao.getById(token.getUser().getId());
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        user.setStatus(true);
        userDao.update(user);
        tokenDao.deactivate(tokenId);
        return true;
    }

}
