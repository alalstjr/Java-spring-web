--------------------
# Java Spring Web
--------------------

# 목차

- [1. 스프링 MVC](#스프링-MVC)
    - [1. MVC 코드 예제](#MVC-코드-예제)
- [2. 서블릿 소개](#서블릿-소개)
    - [1. 서블릿 코드](#서블릿-코드)
- [3. 스프링 IoC 컨테이너 연동](#스프링-IoC-컨테이너-연동)
    - [1. DispatcherServlet](#DispatcherServlet)

# 스프링 MVC

- 스프링 MVC
    - M: 모델 - 평범한 자바 객체 POJO
        - 도메인 객체 또는 DTO로 화면에 전달할 또는 화면에서 전달 받은 데이터를 담고 있는 객체.
    - V: 뷰 - HTML. JSP, 타임리프, ...
        - 데이터를 보여주는 역할. 다양한 형태로 보여줄 수 있다. HTML, JSON, XML, ...
    - C: 컨트롤러 - 스프링 @MVC
        - 사용자 입력을 받아 모델 객체의 데이터를 변경하거나, 모델 객체를 뷰에 전달하는 역할.
        - 입력값 검증
        - 입력 받은 데이터로 모델 객체 변경
        - 변경된 모델 객체를 뷰에 전달

- MVC 패턴의 장점
    - 동시 다발적(Simultaneous) 개발 - 백엔드 개발자와 프론트엔드 `개발자가 독립적으로 개발을 진행`할 수 있다.
    - 높은 결합도 - 논리적으로 관련있는 기능을 하나의 컨트롤러로 묶거나, 특정 모델과 관련있는 뷰를 그룹화 할 수 있다.
    - 낮은 의존도 - 뷰, 모델, 컨트롤러는 각각 독립적이다.
    - 개발 용이성 - 책임이 구분되어 있어 코드 수정하는 것이 편하다.
    - 한 모델에 대한 여러 형태의 뷰를 가질 수 있다.

- MVC 패턴의 단점
    - 코드 네비게이션 복잡함
    - 코드 일관성 유지에 노력이 필요함
    - 높은 학습 곡선

- 참고
    - https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller
    - https://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html

## MVC 코드 예제

> Event.class

~~~
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private String name;

    private int limitOfEnrollment;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
~~~

> EventService.class

~~~
@Service
public class EventService {

    public List<Event> getEvents() {
        Event build1 = Event
                .builder()
                .name("스프링 웹 MVC 1차")
                .limitOfEnrollment(5)
                .startDateTime(LocalDateTime.of(2020, 1, 1, 1, 1))
                .endDateTime(LocalDateTime.of(2020, 2, 2, 2, 2))
                .build();

        Event build2 = Event
                .builder()
                .name("스프링 웹 MVC 2차")
                .limitOfEnrollment(5)
                .startDateTime(LocalDateTime.of(2020, 3, 3, 3, 3))
                .endDateTime(LocalDateTime.of(2020, 4, 4, 4, 4))
                .build();

        return List.of(build1, build2);
    }
}
~~~

> EventController.class

~~~
/**
 * @Controller 어노테이션을 붙는순간 MVC 패턴에서 컨트롤러 역할을 하는 클레스가 됩니다.
 */
@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * 화면에 보여주는 정보를 담는 Map Model
     * <p>
     * addAttribute() : key, value 형식으로 값을 담을 수 있습니다.
     */
    // @RequestMapping(value = "/events", method = RequestMethod.GET)
    @GetMapping("/events")
    public String event(Model model) {
        model.addAttribute("events", eventService.getEvents());

        return "evnets";
    }
}
~~~

> events.html

~~~
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
  <h1>이벤트 목록</h1>
  <table>
    <tr>
      <th>이름</th>
      <th>참가 인원</th>
      <th>시작</th>
      <th>종료</th>
    </tr>
    <tr th:each="event: ${events}">
      <th th:text="${event.name}">이벤트 이름</th>
      <th th:text="${event.limitOfEnrollment}">최대 인원</th>
      <th th:text="${event.startDateTime}">시작 날짜</th>
      <th th:text="${event.endDateTime}">마감 날짜</th>
    </tr>
  </table>
</body>
</html>
~~~

# 서블릿 소개
 
Spring Web MVC 는 서블릿 기반의 웹 에플리케이션을 쉽게 만들 수 있도록 도와주는 프레임워크

- 서블릿 (Servlet)
    - 자바 엔터프라이즈 에디션은 웹 애플리케이션 개발용 스팩과 API 제공.
    - 요청 당 쓰레드 (만들거나, 풀에서 가져다가) 사용
    - 그 중에 가장 중요한 클래스중 하나가 HttpServlet.

- 서블릿 등장 이전에 사용하던 기술인 CGI (Common Gateway Interface)
    - 요청 당 프로세스를 만들어 사용

- 서블릿의 장점 (CGI에 비해)
    - 빠르다.
    - 플랫폼 독립적
    - 보안
    - 이식성

- 서블릿 엔진 또는 서블릿 컨테이너 (톰캣, 제티, 언더토, ...)
    - 세션 관리
    - 네트워크 서비스
    - MIME 기반 메시지 인코딩 디코딩
    - 서블릿 생명주기 관리

- 서블릿 생명주기
    - 서블릿 컨테이너가 서블릿 인스턴스의 init() 메소드를 호출하여 초기화 한다.
        - 최초 요청을 받았을 때 한번 초기화 하고 나면 그 다음 요청부터는 이 과정을 생략한다.
    - 서블릿이 초기화 된 다음부터 클라이언트의 요청을 처리할 수 있다. 각 요청은 별도의 쓰레드로 처리하고 이때 서블릿 인스턴스의 service() 메소드를 호출한다.
        - 이 안에서 HTTP 요청을 받고 클라이언트로 보낼 HTTP 응답을 만든다.
        - service()는 보통 HTTP Method에 따라 doGet(), doPost() 등으로 처리를 위임한다.
        - 따라서 보통 doGet() 또는 doPost()를 구현한다.
    - 서블릿 컨테이너 판단에 따라 해당 서블릿을 메모리에서 내려야 할 시점에 destroy()를 호출한다.

## 서블릿 코드

서블릿 의존성 추가

~~~
  <dependencies>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>4.0.1</version>
      <scope>provided</scope>
    </dependency>
  </dependencies>
~~~

> HelloServlet.class

~~~
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
        resp.getWriter().println("<html>");
        resp.getWriter().println("<h1>Hello Servlet</h1>");
        resp.getWriter().println("</html>");
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
~~~

> web.xml

~~~
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.4"
         xmlns="http://java.sun.com/xml/ns/j2ee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">

  <servlet>
    <servlet-name>hello</servlet-name>
    <servlet-class>me.whiteship.HelloServlet</servlet-class>
  </servlet>

  <servlet-mapping>
    <servlet-name>hello</servlet-name>
    <url-pattern>/hello</url-pattern>
  </servlet-mapping>

</web-app>
~~~

톰켓 설정 후 톰켓으로 실행

# 서블릿 리스너와 필터

- 서블릿 리스너
    - 웹 애플리케이션에서 발생하는 주요 `이벤트(라이프싸이클 변화, 세션의 변화) 를 감지`하고 각 `이벤트에 특별한 작업`이 필요한 경우에 사용할 수 있다.
        - 서블릿 컨텍스트 수준의 이벤트
            - 컨텍스트 라이프사이클 이벤트
            - 컨텍스트 애트리뷰트 변경 이벤트
        - 세션 수준의 이벤트
            - 세션 라이프사이클 이벤트
            - 세션 애트리뷰트 변경 이벤트

- 서블릿 필터
    - 들어온 요청을 서블릿으로 보내고, 또 서블릿이 작성한 응답을 클라이언트로 `보내기 전에 특별한 처리가 필요한 경우`에 사용할 수 있다.
    - 체인 형태의 구조

## 서블릿 코드

> MyListener.class

~~~
public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context Init");
        sce.getServletContext().setAttribute("name","jjunpro");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context Destroyed");
    }
}
~~~

> MyFilter.class

~~~
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter init");
    }

    /**
     * filterChain 으로 다음 필터로 넘어갈 수 있도록 설정해야 합니다.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter doFilter");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("Filter destroy");
    }
}
~~~

> web.xml

~~~
...
  <filter>
    <filter-name>myFilter</filter-name>
    <filter-class>me.whiteship.MyFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>myFilter</filter-name>
    <servlet-name>hello</servlet-name>
  </filter-mapping>

  <listener>
    <listener-class>me.whiteship.MyListener</listener-class>
  </listener>

  <servlet>
    <servlet-name>hello</servlet-name>
    <servlet-class>me.whiteship.HelloServlet</servlet-class>
  </servlet>

  <servlet-mapping>
    <servlet-name>hello</servlet-name>
    <url-pattern>/hello</url-pattern>
  </servlet-mapping>
...
~~~

> HelloServlet.class

~~~
public class HelloServlet extends HttpServlet {
    ...
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("doGet");
        resp.getWriter().println("<html>");
        resp.getWriter().println("<h1>Hello Servlet " + getServletContext().getAttribute("name") + " </h1>");
        resp.getWriter().println("</html>");
    }
    ...
}
~~~

> 라이프싸이클 로그

~~~
Context Init
Filter init
init

Filter doFilter
doGet

destroy
Filter destroy
Context Destroyed
~~~

# 스프링 IoC 컨테이너 연동

> web.xml

~~~
<listener>
<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
~~~

Spring IOC 컨테이너 즉 에플리케이션 컨텍스트를 서블릿 생명주기에 맞춰서 바인딩 해줍니다. 
에플리케이션 컨텍스트를 만들려면 Spring 설정파일이 필요합니다.

- 서블릿 애플리케이션에 스프링 연동하기
    - 서블릿에서 스프링이 제공하는 IoC 컨테이너 활용하는 방법
    - 스프링이 제공하는 서블릿 구현체 DispatcherServlet 사용하기

- ContextLoaderListener
    - 서블릿 리스너 구현체
    - ApplicationContext를 만들어 준다.
    - ApplicationContext를 서블릿 컨텍스트 라이프사이클에 따라 등록하고 소멸시켜준다.
    - 서블릿에서 IoC 컨테이너를 ServletContext를 통해 꺼내 사용할 수 있다.

> AppConfig.class

~~~
@Configuration
@ComponentScan
public class AppConfig { }
~~~

자바 설정파일

> HelloService.class

~~~
@Service
public class HelloService {

    public String getName() {
        return "jjunpro";
    }
}
~~~

서블릿 컨텍스트에 등록할 Bean 클레스

> web.xml

~~~
  Spring IOC 컨테이너 즉 에플리케이션 컨텍스트를 서블릿 생명주기에 맞춰서 바인딩 해줍니다.

  ContextLoaderListener 사용하는 params 들이 존재합니다.
  컨텍스트의 설정파일 위치, 에플리케이션 컨텍스트의 타입지정 등등..

  contextClass & contextConfigLocation 두개를 활용해서 ContextLoaderListener 가 AnnotationConfigWebApplicationContext (ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) 를 만듭니다.
  만들어진 웹 에플리케이션 컨텍스트 내부에는 HelloService 가 Bean 으로 들어있게 됩니다.

  그러면 서블릿에서 에플리케이션을 통해서 HelloService 를 꺼내서 사용할 수 있습니다.

  context 설정은 다른 필터보다 위에 위치해야 합니다.

  <context-param>
    <param-name>contextClass</param-name>
    <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
  </context-param>

  Java 설정파일 등

  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>me.whiteship.AppConfig</param-value>
  </context-param>

  웹 에플리케이션 컨텍스트를 서블릿 컨텍스트에 등록해주는 리스너
  서블릿 컨텍스트 란 모든 서블릿들이 사용할 수 있는 공용 저장소

  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
~~~

> ContextLoaderListener.class

~~~
서블릿 컨텍스트가 생성되는 시점에 웹 에플리케이션을 생성해서 서블릿 컨텍스트에 등록되는 과정이
initWebApplicationContext() 메소드에서 일어나게 됩니다.

public void contextInitialized(ServletContextEvent event) {
    this.initWebApplicationContext(event.getServletContext());
}
~~~

> HelloServlet.class

~~~
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    System.out.println("doGet");

    ApplicationContext attribute = (ApplicationContext) getServletContext()
            .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    HelloService bean = attribute.getBean(HelloService.class);

    resp.getWriter().println("<html>");
    resp.getWriter().println("<h1>서블릿 에플리케이션 컨텍스 " + bean.getName() + " </h1>");
    resp.getWriter().println("</html>");
}
~~~

## DispatcherServlet

서블릿 컨텍스트를 만들며 요청을 처리할 때마다 서블릿을 하나씩 만들 때 `공통으로 사용하고싶은 부분은 Front Controller 디자인 패턴`으로 해결합니다.

Front Controller 는 `모든 요청을 하나의 컨트롤러에서 받아서 처리`하는 것입니다.
Front Controller 에서 해당 요청을 처리할 `핸들러들한테 분배`를 하는것 이를 `(디스패치)` 라고 합니다.

이를 스프링에서 핵심이되는 클래스 `스프링이 제공하는 서블릿 구현체 DispatcherServlet` 입니다.

DispatcherServlet 은 서블릿 컨텍스트에 등록되어 있는 (ROOT) 웹 에플리케이션을 상속받는 `(서블릿) 웹 에플리케이션을 하나 더 생성`합니다.

상속관계로 해서 생성하는 이유

서블릿 컨텍스트(ContextLoaderListener) 에서 만든 웹 에플리케이션 컨텍스트는 여러 다른 서블릿에서도 공용해서 사용할 수 있습니다.
(ROOT) 웹 에플리케이션은 다른 서블릿도 공용으로 사용할 수 있습니다.

DispatcherServlet 내부에서 만든 에플리케이션은 `해당 DispatcherServlet 내부에서만 사용이 가능`합니다.

혹시라도 DispatcherServlet 을 여러개를 만드는 경우 `여러 DispatcherServlet 들이 서로 공용으로 써야하는 Bean 존재하는 경우를 커버`하기 위해서
`상속구조`를 만들 수 있게 되어있는 것입니다.

(ROOT) 웹 에플리케이션 내부에는 Web 과 관련된 Bean 들이 존재하지 않습니다.
이유는 `Service & Repository 는 다른 DispatcherServlet 에서도 공용으로 사용할 수 있는 Bean` 이라서 그렇습니다.

DispatcherServlet 에서 만드는 (서블릿) 웹 에플리케이션은 웹과 관련된 Bean들이 등록이 되어있습니다.
`Controller & ViewResolver & HandlerMapping 이러한 Bean들은 해당 DispatcherServlet 에서만 한정적`입니다.
