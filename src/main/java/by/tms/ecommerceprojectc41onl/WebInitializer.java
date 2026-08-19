package by.tms.ecommerceprojectc41onl;

import by.tms.ecommerceprojectc41onl.config.WebConfiguration;
import jakarta.servlet.ServletRegistration;
import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?> @Nullable [] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?> @Nullable [] getServletConfigClasses() {
        return new Class[]{WebConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("throwExceptionIfNoHandlerFound","true");
    }
}
