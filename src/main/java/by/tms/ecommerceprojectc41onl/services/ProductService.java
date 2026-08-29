package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.*;
import by.tms.ecommerceprojectc41onl.dto.CreateProductDto;
import by.tms.ecommerceprojectc41onl.dto.ProductCardDto;
import by.tms.ecommerceprojectc41onl.model.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

        return sellerDao.getByUserId(user.getId());
    }

    private File createFile(CreateProductDto createProductDto) {
        File file = new File();
        file.setFileName(createProductDto.fileData().fileName());
        file.setFile(createProductDto.fileData().data());

        return fileDao.create(file);
    }

    // TODO : Реализовать
    // Наш новый метод для фильтрации
    public List<ProductCardDto> getProductsByCategories(List<Long> categoryIds) {
        return productDao.getByCategories(categoryIds)
                .stream()
                .map(this::mapToProductCardDto)
                .toList();
    }

    // TODO : Реализовать
    public List<ProductCardDto> getAllProductCards() {
        return productDao.getAll()
                .stream()
                .map(this::mapToProductCardDto)
                .toList();
    }

    // Общий приватный метод для преобразования Product в ProductCardDto
    private ProductCardDto mapToProductCardDto(Product product) {
        Long photoId = productPhotoDao
                .getPhotoIdByProductId(product.getId())
                .orElse(null);

        return new ProductCardDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                photoId,
                0.0,      // score — пока нет рейтинга
                false     // favourite — пока нет избранного
        );
    }
}
