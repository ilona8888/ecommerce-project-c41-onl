package by.tms.ecommerceprojectc41onl.dto;


import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class TestController {
 @PostMapping("/registration")
 public String test(@Valid RegistrationUserDTO registrationUserDTO, BindingResult bindingResult, Model model) {
  if (bindingResult.hasErrors()) {
   model.addAttribute("message", "invalid input");
   return "registration";
  }
  String name = registrationUserDTO.getName();
     return "redirect:/registration";
 }
@GetMapping("/index")
public String index(Model model){

model.addAttribute("newProduct", "ProductDTO");
return "index";
}

 @PostMapping("/index")
 public String test2(@Valid ProductDTO productDTO, BindingResult bindingResult, Model model) {
  if (bindingResult.hasErrors()) {
   model.addAttribute("message", "invalid input");
   return "index";
  }
  return "redirect:/index";
 }
}
