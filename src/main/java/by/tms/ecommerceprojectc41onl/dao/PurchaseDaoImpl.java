package by.tms.ecommerceprojectc41onl.dao;

import by.tms.ecommerceprojectc41onl.dao.interfaces.PurchaseDao;
import by.tms.ecommerceprojectc41onl.model.Purchase;
import by.tms.ecommerceprojectc41onl.model.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PurchaseDaoImpl implements PurchaseDao {

    // Если используете базу данных, внедрите DataSource по аналогии с ProductDao:
    // private final DataSource dataSource;
    // public PurchaseDaoImpl(DataSource dataSource) { this.dataSource = dataSource; }

    @Override
    public void save(Purchase purchase) {
        // Код сохранения покупки (JDBC / InMemory)
    }

    @Override
    public List<Purchase> findAll() {
        return List.of();
    }

    @Override
    public Purchase findById(Long aLong) {
        return null;
    }

    @Override
    public List<Purchase> findByUser(User user) {
        // Код получения покупок пользователя
        return List.of();
    }
}