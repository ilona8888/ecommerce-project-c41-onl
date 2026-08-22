/**
 * Classname    SellerPageController
 * @version     0.01
 * @author      Aleksei Borzetsov
 * date         22.08.2026
 */

package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dao.SellerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static by.tms.ecommerceprojectc41onl.model.UserRole.SELLER;

@Controller
public class SellerController {

    @Autowired
    SellerDao sellerDao;

    @GetMapping("/sellerPage/{sellerID}")  //GET localhost:8080/sellerPage/<ID>
    public String GetSellerPage(@PathVariable("sellerID") String sellerID, Model model) {

        String sellerDetails = sellerDao.getInfoAboutLegalEntity(Long.parseLong(sellerID));
        model.addAttribute("sellerDetails", sellerDetails);

        String sellerContactInfo = sellerDao.getContactInfo(Long.parseLong(sellerID));
        model.addAttribute("sellerContactInfo", sellerContactInfo);
        model.addAttribute("userRole", SELLER);

        return "profile";
    }

    @PostMapping("/sellerPage/{sellerID}")  //POST localhost:8080/sellerPage/<ID>?legalEntity=<val>&contactInfo=<val>
    public String UpdateSeller(@PathVariable("sellerID") String sellerID,
                               @RequestParam("legalEntity") String legalEntity,
                               @RequestParam("contactInfo") String contactInfo) {
        boolean setInfoAboutLegalEntityStatus = sellerDao.setInfoAboutLegalEntity(Long.parseLong(sellerID), legalEntity);
        boolean setContactInfoStatus = sellerDao.setContactInfo(Long.parseLong(sellerID), contactInfo);

        return "redirect:/sellerPage/" + sellerID;
    }
}
