package by.tms.ecommerceprojectc41onl.filter;

import by.tms.ecommerceprojectc41onl.model.User;
import by.tms.ecommerceprojectc41onl.model.UserRole;
import by.tms.ecommerceprojectc41onl.services.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component("authFilter")
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private final SessionService sessionService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //восстановление из куки
        sessionService.checkMeOut(req);

        String path = req.getRequestURI();
        User user = sessionService.getCurrentUser(req.getSession());


        // Гость
        if (user == null) {

            // TODO : пути пересмотреть
            if (path.startsWith("/favorites") ||
                    path.startsWith("/orders") ||
                    path.startsWith("/seller") ||
                    path.startsWith("/admin")) { // TODO: Реализовать пользователя функционал страницы Admin(создает только категории)

                res.sendRedirect("/login");
                return;
            }

            chain.doFilter(req, res);
            return;
        }

        // Залогиненный пользователь
        UserRole role = user.getRole();

        // ADMIN зона
        if (path.startsWith("/admin") && role != UserRole.ADMIN) {
            res.sendRedirect("/403");
            return;
        }

        // SELLER зона
        if (path.startsWith("/seller") && role != UserRole.SELLER) {
            res.sendRedirect("/403");
            return;
        }

        // TODO : пути пересмотреть
        // Покупательские зоны — доступны BUYER и SELLER
        if ((path.startsWith("/favorites") ||
                path.startsWith("/product") ||
                path.startsWith("/cart"))
                && role == UserRole.ADMIN) {

            res.sendRedirect("/403");
            return;
        }

        chain.doFilter(req, res);
    }
}
