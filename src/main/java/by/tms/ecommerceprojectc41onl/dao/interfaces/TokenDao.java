package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.Token;

import java.util.Optional;
import java.util.UUID;

public interface TokenDao {
    void save(Token token);
    Optional<Token> findById(UUID id);


}
