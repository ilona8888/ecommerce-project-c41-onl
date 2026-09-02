/**
 * Classname    SellerPageController
 * @version     0.01
 * @author      Aleksei Borzetsov
 * date         22.08.2026
 */

package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dao.SellerDao;
import by.tms.ecommerceprojectc41onl.dao.UserDao;
import by.tms.ecommerceprojectc41onl.dto.SellerPageDto;
import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.model.UserRole;
import by.tms.ecommerceprojectc41onl.services.SellerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static by.tms.ecommerceprojectc41onl.model.UserRole.SELLER;

@Controller
public class SellerController {

    @Autowired
    SellerDao sellerDao;

    @Autowired
    SellerService sellerService;

    @Autowired
    UserDao userDao; // <-- ЭТОГО ПОЛЯ НЕ ХВАТАЛО В ВАШЕМ КОДЕ, ИЗ-ЗА ЭТОГО ОШИБКА КОМПИЛЯЦИИ

    @GetMapping("/sellerPage/{sellerID}")  //GET localhost:8080/sellerPage/<ID>
    public String GetSellerPage(@NotEmpty @NotBlank @PathVariable("sellerID") String sellerID, Model model) {

        //Получить из БД данные о продавце
        SellerPageDto sellerPageDto = new SellerPageDto();
        sellerPageDto.setLegalEntity(sellerDao.getInfoAboutLegalEntity(Long.parseLong(sellerID)));
        sellerPageDto.setContactInfo(sellerDao.getContactInfo(Long.parseLong(sellerID)));
        model.addAttribute("sellerPageDto", sellerPageDto);

        //Thymeleaf Reference для формы HTML
        model.addAttribute("newSellerPageDto", new SellerPageDto());
        //Роль пользователя, чтобы различать покупателя и продавца
        model.addAttribute("userRole", SELLER);
        //Id продавца для динамического пути в th:action
        model.addAttribute("sellerID", sellerID);

        return "profile";
    }

    @PostMapping("/sellerPage/{sellerID}")
    public String UpdateSeller(@NotEmpty @NotBlank @PathVariable("sellerID") String sellerID,
                               @ModelAttribute("newSellerPageDto") @Valid SellerPageDto sellerPageDto,
                               BindingResult result, Model model, HttpSession session) {

        long id = Long.parseLong(sellerID);

        // Получить из БД данные о продавце для отображения в случае ошибки
        SellerPageDto oldSellerPageDto = new SellerPageDto();
        oldSellerPageDto.setLegalEntity(sellerDao.getInfoAboutLegalEntity(id));
        oldSellerPageDto.setContactInfo(sellerDao.getContactInfo(id));

        // Проверить ввод пользователя
        if (result.hasErrors()) {
            model.addAttribute("sellerPageDto", oldSellerPageDto);
            model.addAttribute("newSellerPageDto", sellerPageDto); // Возвращаем введенное, чтобы не стиралось
            model.addAttribute("userRole", SELLER);
            model.addAttribute("sellerID", sellerID);
            return "profile";
        }

        // Обновить данные о продавце
        boolean setInfoAboutLegalEntityStatus = sellerDao.setInfoAboutLegalEntity(id, sellerPageDto.getLegalEntity());
        boolean setContactInfoStatus = sellerDao.setContactInfo(id, sellerPageDto.getContactInfo());

        if (setInfoAboutLegalEntityStatus && setContactInfoStatus) {
            // Обновляем сессию актуальным пользователем из базы
            userDao.getById(id).ifPresent(updatedUser -> {
                session.setAttribute("user", updatedUser);
            });

            return "redirect:/sellerPage/" + sellerID;
        } else {
            model.addAttribute("updateError", "Не удалось обновить информацию о продавце");
            model.addAttribute("sellerPageDto", oldSellerPageDto);
            model.addAttribute("userRole", SELLER);
            model.addAttribute("sellerID", sellerID);
            return "profile";
        }
    }

    @PostMapping("/profile/become-seller/{id}")
    public String registerSeller(@PathVariable("id") long id, HttpSession session) {
        // 1. Меняем роль в базе данных и создаем запись в SELLERS
        sellerService.changeRoleToSeller(id);

        // 2. Достаем обновленного пользователя и записываем по правильному ключу сессии
        userDao.getById(id).ifPresent(updatedUser -> {
            // Замените "user" на то значение, которое лежит в константе CURRENT_USER (например, "currentUser")
            session.setAttribute("currentUser", updatedUser);
        });

        // 3. Редирект на страницу профиля продавца
        return "redirect:/sellerPage/" + id;
    }
}
