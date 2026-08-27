package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.UserDao;
import by.tms.ecommerceprojectc41onl.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public User authenticate(String email, String password) throws AuthenticationException {
        //Поиск пользователя по email
        Optional<User> userOptional = userDao.findByEmail(email);

        // Если пользователя нет, выбрасываем общую ошибку
        if (userOptional.isEmpty()) {
            throw new AuthenticationException("Incorrect email or password!");
        }

        User user = userOptional.get();

//        //Сверяем хэш пароля //TODO : раскомментировать после реализации регистрации
//        if (!checkPassword(password,user.getPasswordHash())) {
//            throw new AuthenticationException("Incorrect email or password!!");
//        }

        if(!user.getPasswordHash().equals(password)) {
            throw new AuthenticationException("Incorrect email or password!!");
        }

        // 3. Критерий: Неподтверждённый аккаунт (status=false) -> вход отклонён
        if (!user.isStatus()) {
            throw new AuthenticationException("Incorrect email or password!!!");
        }

        return user;
    }

    public boolean checkPassword(String rawPassword, String storedHash) {
        return passwordEncoder.matches(rawPassword, storedHash);
    }
}
