package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.TokenDao;
import by.tms.ecommerceprojectc41onl.dao.UserDao;
import by.tms.ecommerceprojectc41onl.model.Token;
import by.tms.ecommerceprojectc41onl.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecoveryService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final long TOKEN_EXPIRY_DAYS = 1;

    public String createRecoveryLink(String email, String baseUrl) {
        Optional<User> userOptional = userDao.findByEmail(email);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        UUID tokenId = UUID.randomUUID();
        Token token = new Token();
        token.setId(tokenId);
        token.setType("reset_password");
        token.setActive(true);
        token.setUser(user);
        token.setCreatedDate(LocalDate.now());
        tokenDao.save(token);
        return baseUrl + "/recovery/reset?token=" + tokenId;
    }

    public boolean validateToken(String tokenStr) {
        if (tokenStr == null || tokenStr.trim().isEmpty()) {
            return false;
        }
        try {
            UUID tokenId = UUID.fromString(tokenStr);
            Optional<Token> tokenOptional = tokenDao.findById(tokenId);
            if (tokenOptional.isEmpty()) {
                return false;
            }
            Token token = tokenOptional.get();
            if (!token.isActive()) {
                return false;
            }
            LocalDate expiryTime = token.getCreatedDate().plusDays(TOKEN_EXPIRY_DAYS);
            return !LocalDate.now().isAfter(expiryTime);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean resetPassword(String tokenStr, String newPassword) {
        if (!validateToken(tokenStr)) {
            return false;
        }
        String regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*\\(\\),\\.\\?\\\":\\{\\}\\|<>_])[A-Za-z\\d!@#\\$%\\^&\\*\\(\\),\\.\\?\\\":\\{\\}\\|<>_]{8,20}$";
        // Правило валидации пароля
        if (newPassword == null || !newPassword.matches(regexp)) {
            return false;
        }

        UUID tokenId = UUID.fromString(tokenStr);
        Token token = tokenDao.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Токен восстановления не найден."));

        Optional<User> userOptional = userDao.getById(token.getUser().getId());
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userDao.update(user);
        tokenDao.deactivate(tokenId);
        return true;
    }
}
