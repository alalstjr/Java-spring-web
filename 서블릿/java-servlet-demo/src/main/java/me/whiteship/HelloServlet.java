package me.whiteship;

import static org.springframework.web.context.WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * HttpServlet 상속 받아야 Servlet 될 수 있습니다.
 * <p>
 * 해당 HttpServlet 실행 방법은 톰켓이 필요합니다.
 */
public class HelloServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("doGet");

        ApplicationContext attribute = (ApplicationContext) getServletContext()
                .getAttribute(ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        HelloService bean = attribute.getBean(HelloService.class);

        resp.getWriter().println("<html>");
        resp.getWriter().println("<h1>Hello Servlet " + getServletContext().getAttribute("name") + " </h1>");
        resp.getWriter().println("<h1>서블릿 에플리케이션 컨텍스 " + bean.getName() + " </h1>");
        resp.getWriter().println("</html>");
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
