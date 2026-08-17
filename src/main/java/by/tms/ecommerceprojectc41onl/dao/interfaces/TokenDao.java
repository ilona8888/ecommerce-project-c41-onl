package by.tms.ecommerceprojectc41onl.dao.interfaces;

import by.tms.ecommerceprojectc41onl.model.Token;

import java.util.UUID;

public interface TokenDao extends GenericDao<Token, UUID> {

    void deactivate(UUID id);
}
