/**
 * Classname    SellerPageController
 * @version     0.01
 * @author      Aleksei Borzetsov
 * date         22.08.2026
 */

package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dao.SellerDao;
import by.tms.ecommerceprojectc41onl.dto.SellerPageDto;
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

    @PostMapping("/sellerPage/{sellerID}")  //POST localhost:8080/sellerPage/<ID>?legalEntity=<val>&contactInfo=<val>
    public String UpdateSeller(@NotEmpty @NotBlank @PathVariable("sellerID") String sellerID,
                               @ModelAttribute("newSellerPageDto") @Valid SellerPageDto  sellerPageDto,
                               BindingResult result, Model model) {
        //Получить из БД данные о продавце для отображения в случае ошибки или неудачи при обновлении
        SellerPageDto oldSellerPageDto = new SellerPageDto();
        oldSellerPageDto.setLegalEntity(sellerDao.getInfoAboutLegalEntity(Long.parseLong(sellerID)));
        oldSellerPageDto.setContactInfo(sellerDao.getContactInfo(Long.parseLong(sellerID)));

        //Проверить ввод пользователя
        if (result.hasErrors()) {
            model.addAttribute("sellerPageDto", oldSellerPageDto);
            return "profile";
        }

        //Обновить данные о продавце
        boolean setInfoAboutLegalEntityStatus = sellerDao.setInfoAboutLegalEntity(Long.parseLong(sellerID),
                sellerPageDto.getLegalEntity());
        boolean setContactInfoStatus = sellerDao.setContactInfo(Long.parseLong(sellerID),
                sellerPageDto.getContactInfo());

        if (setInfoAboutLegalEntityStatus & setContactInfoStatus)
            return "redirect:/sellerPage/" + sellerID;  //Если удалось обновить информацию, перенаправить на страницу
        else {
            //Если не удалось, отобразить ошибку на этой же странице
            model.addAttribute("updateError", "Не удалось обновить информацию о продавце");
            return "profile";
        }
    }
}
