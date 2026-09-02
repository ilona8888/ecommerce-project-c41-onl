package by.tms.ecommerceprojectc41onl.controller;

import by.tms.ecommerceprojectc41onl.dto.CategoryDto;
import by.tms.ecommerceprojectc41onl.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final CategoryService categoryService;

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("categoryDto", new CategoryDto(null, ""));
        return "adminPage";
    }

    @PostMapping("/new-category")
    public String createCategory(@Valid CategoryDto categoryDto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            // При ошибке валидации возвращаем ту же страницу
            model.addAttribute("categoryDto", categoryDto);
            return "adminPage";
        }
        categoryService.save(categoryDto);
        return "redirect:/admin";
    }

}
