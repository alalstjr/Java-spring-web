package me.whiteship;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebApplication implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(servletContext);
        /* 애플리케이션 컨텍스트 설정을 등록합니다. */
        applicationContext.register(WebConfig.class);
        applicationContext.refresh();

        /* DispatcherServlet 생성 */
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        Dynamic           app               = servletContext.addServlet("app", dispatcherServlet);
        /* app 이하의 모든요청을 맵핑합니다. */
        app.addMapping("/app/*");
    }
}
