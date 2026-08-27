package by.tms.ecommerceprojectc41onl.interceptor;

import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.services.CategoryService;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class GlobalModelInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;
    private final CategoryService categoryService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            HttpSession session = request.getSession(false);
            User user = (session != null) ? sessionService.getCurrentUser(session) : null;
            modelAndView.addObject("user", user);

            // Категории (только если контроллер их не положил сам)
            if (!modelAndView.getModel().containsKey("categories")) {
                modelAndView.addObject("categories", categoryService.findAllCategories());
            }
        }
    }
}
