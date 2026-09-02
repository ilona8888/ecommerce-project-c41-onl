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
import java.util.Set;

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
     * Карточки всех товаров каталога.
     *
     * @param user текущий пользователь (может быть null — гость)
     * @return список карточек, у гостя все сердечки пустые
     */
    public List<ProductCardDto> getAllProductCards(@Nullable User user) {

        Set<Long> favouriteIds = getFavouriteIds(user);

        return productDao.getAll()
                .stream()
                .map(product -> toCard(product, favouriteIds))
                .toList();
    }

    /**
     * Карточки товаров, которые пользователь добавил в избранное.
     *
     * @param user текущий пользователь
     * @return список карточек, у всех сердечко закрашено
     */
    public List<ProductCardDto> getFavouriteProductCards(User user) {

        Set<Long> favouriteIds = getFavouriteIds(user);

        return favouriteDao.findFavoritesByUser(user.getId())
                .stream()
                .map(product -> toCard(product, favouriteIds))
                .toList();
    }

    /**
     * ID избранных товаров пользователя одним запросом,
     * чтобы не дёргать БД на каждую карточку.
     */
    private Set<Long> getFavouriteIds(@Nullable User user) {
        return (user == null || user.getId() == null)
                ? Set.of()
                : favouriteDao.findFavouriteProductIds(user.getId());
    }

    private ProductCardDto toCard(Product product, Set<Long> favouriteIds) {

        Long photoId = productPhotoDao
                .getPhotoIdByProductId(product.getId())
                .orElse(null);

        return new ProductCardDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                photoId,
                0.0,                                    // score — пока нет рейтинга
                favouriteIds.contains(product.getId())  // товар в избранном у текущего пользователя
        );
    }
}
