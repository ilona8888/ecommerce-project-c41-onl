package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.*;
import by.tms.ecommerceprojectc41onl.dto.CreateProductDto;
import by.tms.ecommerceprojectc41onl.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Сервис для работы с товарами.
 *
 * @author Ирина Мизгир
 * @date 16.08.2026 18:31
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDao productDao;

    private final UserDao userDao;

    private final SellerDao sellerDao;

    private final CategoryDao categoryDao;

    private final FileDao fileDao;

    private final ProductPhotoDao productPhotoDao;

    /**
     * Создание нового товара.
     *
     * @param createProductDto DTO для создания нового товара
     */
    public void create(CreateProductDto createProductDto) {
        ProductPhoto photo = new ProductPhoto();
        photo.setFile(createFile(createProductDto));
        photo.setProduct(createProduct(createProductDto));
        productPhotoDao.create(photo);
    }

    private Product createProduct(CreateProductDto createProductDto) {
        Seller seller = getSeller(createProductDto);

        Category category = categoryDao.getById(createProductDto.categoryId());

        Product product = new Product();
        product.setName(createProductDto.name());
        product.setPrice(BigDecimal.valueOf(createProductDto.price()));
        product.setDescription(createProductDto.description());

        return productDao.create(product, seller, category);
    }

    private Seller getSeller(CreateProductDto createProductDto) {
        User user = userDao.getByName(createProductDto.userName());

        return sellerDao.getById(user.getId());
    }

    private File createFile(CreateProductDto createProductDto) {
        File file = new File();
        file.setFileName(createProductDto.fileData().fileName());
        file.setFile(createProductDto.fileData().data());

        return fileDao.create(file);
    }

}
