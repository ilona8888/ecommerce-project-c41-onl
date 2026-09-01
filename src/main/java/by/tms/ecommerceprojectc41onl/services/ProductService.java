package by.tms.ecommerceprojectc41onl.services;

import by.tms.ecommerceprojectc41onl.dao.*;
import by.tms.ecommerceprojectc41onl.dto.CreateProductDto;
import by.tms.ecommerceprojectc41onl.dto.FileData;
import by.tms.ecommerceprojectc41onl.dto.ProductCardDto;
import by.tms.ecommerceprojectc41onl.model.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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

    private final ReviewDao reviewDao;

    private final FavouriteDao favouriteDao;

    /**
     * Создание нового товара.
     *
     * @param createProductDto DTO для создания нового товара
     */
    public void create(CreateProductDto createProductDto) {
        Product product = createProduct(createProductDto);
        FileData fileData = createProductDto.fileData();
        if (ArrayUtils.isNotEmpty(fileData.data()) && StringUtils.isNotEmpty(fileData.fileName())) {
            ProductPhoto photo = new ProductPhoto();
            photo.setFile(createFile(createProductDto));
            photo.setProduct(product);
            productPhotoDao.create(photo);
        }
    }

    /**
     * Создание продукта.
     * @param createProductDto Dto для создания продукта.
     * @return Товар.
     */
    private Product createProduct(CreateProductDto createProductDto) {
        Seller seller = getSeller(createProductDto);

        Category category = categoryDao.getById(createProductDto.categoryId());

        Product product = new Product();
        product.setName(createProductDto.name());
        product.setPrice(BigDecimal.valueOf(createProductDto.price()));
        product.setDescription(createProductDto.description());

        return productDao.create(product, seller, category);
    }

    /**
     * Получение продавца.
     * @param createProductDto Dto для создания продукта.
     * @return Продавец.
     */
    private Seller getSeller(CreateProductDto createProductDto) {
        User user = userDao.getByName(createProductDto.userName());

        return sellerDao.getByUserId(user.getId());
    }
    /**
     * Получение файла.
     * @param createProductDto Dto для создания продукта.
     * @return Файл.
     */
    private File createFile(CreateProductDto createProductDto) {
        File file = new File();
        file.setFileName(createProductDto.fileData().fileName());
        file.setFile(createProductDto.fileData().data());

        return fileDao.create(file);
    }

    /**
     * Получение всех карточек товара.
     * @param currentUser Авторизованный пользователь (null - если не авторизованный).
     * @return Список карточек товара.
     */
    public List<ProductCardDto> getAllProductCards(@Nullable User currentUser) {
        return productDao.getAll()
                .stream()
                .map(product -> {

                    Long photoId = productPhotoDao
                            .getPhotoIdByProductId(product.getId())
                            .orElse(null);

                    return new ProductCardDto(
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getDescription(),
                            photoId,
                            reviewDao.getProductRating(product.getId()),
                            isFavoriteProduct(currentUser, product)     // favourite — пока нет избранного
                    );
                })
                .toList();
    }

    /**
     * Проверка, что товар добавлен в избранное.
     * @param user Авторизованный пользователь.
     * @param product Товар.
     * @return Результат проверки.
     */
    private boolean isFavoriteProduct(@Nullable User user, Product product) {
        if (user == null) {
            return false;
        }

        return favouriteDao.exists(user.getId(), product.getId());

    }
}
