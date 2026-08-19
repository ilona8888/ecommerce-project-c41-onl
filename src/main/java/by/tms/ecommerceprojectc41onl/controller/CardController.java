package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dto.CreateProductDto;
import by.tms.ecommerceprojectc41onl.dto.CreateProductRequestDto;
import by.tms.ecommerceprojectc41onl.dto.FileData;
import by.tms.ecommerceprojectc41onl.services.CategoryService;
import by.tms.ecommerceprojectc41onl.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Контроллер для работы с карточками товаров.
 *
 * @author Ирина Мизгир
 * @date 16.08.2026 15:28
 */
@Controller
@RequiredArgsConstructor
public class CardController {

    private final CategoryService categoryService;

    private final ProductService productService;

    /**
     * Создание новой карточки товара.
     *
     * @param model модель
     * @return View создания новой карточки
     */
    @GetMapping("/new-card")
    public String newCard(Model model) {
        model.addAttribute("createProductRequestDto", new CreateProductRequestDto());
        model.addAttribute("categories", categoryService.findAllCategories());

        return "new-card";
    }

    /**
     * Создание нового товара.
     *
     * @param createProductRequestDto данные запроса для создания нового товара
     * @param userDetails информация о текущем авторизованном пользователе
     * @return редирект на главную страницу
     * @throws IOException исключение при чтении данных файлов
     */
    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String create(
            @ModelAttribute("createProductRequestDto") CreateProductRequestDto createProductRequestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {

        MultipartFile multipartFile = createProductRequestDto.getFile();

        var fileData = new FileData(
                multipartFile.getOriginalFilename(),
                multipartFile.getBytes()
        );

        var createProductDto = new CreateProductDto(
                createProductRequestDto.getName(),
                createProductRequestDto.getDescription(),
                createProductRequestDto.getCategoryId(),
                createProductRequestDto.getPrice(),
                fileData,
                userDetails.getUsername()
        );

        productService.create(createProductDto);

        return "redirect:/";
    }


}
