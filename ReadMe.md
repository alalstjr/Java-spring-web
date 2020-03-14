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
- [4. DispatcherServlet 동작 원리](#DispatcherServlet-동작-원리)
- [5. DispatcherServlet](#DispatcherServlet)
  - [1. DispatcherServlet View 존재하는 경우](#DispatcherServlet-View-존재하는-경우)
  - [2. 다른방식의 등록](#다른방식의-등록)
- [6. 커스텀 ViewResolver 등록](#커스텀-ViewResolver-등록)
  - [1. ViewResolver Bean 등록해서 사용해보기](#ViewResolver-Bean-등록해서-사용해보기)
- [7. 스프링 MVC 구성 요소](#스프링-MVC-구성-요소)
- [8. 스프링 MVC 동작 원리](#스프링-MVC-동작-원리)
  - [1. web xml 설정없이 DispatcherServlet](#web-xml-설정없이-DispatcherServlet)
- [9. @EnableWebMvc](#@EnableWebMvc)
  - [1. 디버깅](#디버깅)
- [10. @EnableWebMvc 어노테이션 사용시 설정 추가 방법 (WebMvcConfigurer Interface)](#@EnableWebMvc-어노테이션-사용시-설정-추가-방법-(WebMvcConfigurer-Interface))  
- [11. 스프링 부트의 스프링 MVC 설정](#스프링-부트의-스프링-MVC-설정)
- [12. 스프링 부트 JSP](#스프링-부트-JSP)
  - [1. WAR 파일 배포하기](#WAR-파일-배포하기)
- [13. 도메인 클래스 컨버터](#도메인-클래스-컨버터)
- [14. 핸들러 인터셉터](#핸들러-인터셉터)
  - [1. 핸들러 인터셉터 구현](#핸들러-인터셉터-구현)
- [15. 리소스 핸들러](#리소스-핸들러)
- [16. HTTP 메시지 컨버터](#HTTP-메시지-컨버터)
  - [1. JSON](#JSON)
  - [2. XML](#XML)
- [17. 그밖에 WebMvcConfigurer 설정](#그밖에-WebMvcConfigurer-설정)
- [18. 요청 맵핑하기 HTTP Method](#요청-맵핑하기-HTTP-Method)
- [19. 요청 맵핑하기 URI 패턴](#요청-맵핑하기-URI-패턴)
- [20. HTTP 요청 맵핑하기 미디어 타입 맵핑](#HTTP-요청-맵핑하기-미디어-타입-맵핑)
- [21. 요청 맵핑하기 헤더와 매개변수](#요청-맵핑하기-헤더와-매개변수)
- [22. HTTP 요청 맵핑하기 HEAD와 OPTIONS 요청 처리](#HTTP-요청-맵핑하기-HEAD와-OPTIONS-요청-처리)
- [22. 요청 맵핑하기 커스텀 애노테이션](#요청-맵핑하기-커스텀-애노테이션)
- [23. 핸들러 메소드 아규먼트와 리턴 타입](#핸들러-메소드-아규먼트와-리턴-타입)
- [24. 핸들러 메소드 URI 패턴](#핸들러-메소드-URI-패턴)
- [25. 핸들러 메소드 @RequestMapping](#핸들러-메소드-@RequestMapping)
- [26. 폼 서브밋 (타임리프)](#폼-서브밋-(타임리프))

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

- 기존의 Servlet
  - 기존의 방식은 요청 url당 servlet을 생성하고 그에맞는 Controller에게 요청을 보내주는 코드를 각각 다 따로 작성해야 했습니다.
  - 요청 1 -> Servlet 1 -> Controller 1
  - 요청 2 -> Servlet 2 -> Controller 2
  - 요청 3 -> Servlet 3 -> Controller 3

- Front Controller 패턴 적용
  - FrontController 패턴을 적용하면 하나의 Servlet에서 모든 요청을 받아들여 적절한 Controller로 요청을 위임해줍니다.
  - 요청 1, 2, 3 -> Front Controller -> Controller 1, 2, 3

서블릿 컨텍스트를 만들며 요청을 처리할 때마다 서블릿을 하나씩 만들 때 `공통으로 사용하고싶은 부분은 Front Controller 디자인 패턴`으로 해결합니다.

Front Controller 는 `모든 요청을 하나의 컨트롤러에서 받아서 처리`하는 것입니다.
Front Controller 에서 해당 요청을 처리할 `핸들러들한테 분배`를 하는것 이를 `(디스패치)` 라고 합니다.

이를 스프링에서 핵심이되는 클래스 `스프링이 제공하는 서블릿 구현체 DispatcherServlet` 입니다.

DispatcherServlet 은 서블릿 컨텍스트에 등록되어 있는 (Root) 웹 에플리케이션을 상속받는 `(서블릿) 웹 에플리케이션을 하나 더 생성`합니다.

상속관계로 해서 생성하는 이유

서블릿 컨텍스트(ContextLoaderListener) 에서 만든 웹 에플리케이션 컨텍스트는 여러 다른 서블릿에서도 공용해서 사용할 수 있습니다.
(Root) 웹 에플리케이션은 다른 서블릿도 공용으로 사용할 수 있습니다.

DispatcherServlet 내부에서 만든 에플리케이션은 `해당 DispatcherServlet 내부에서만 사용이 가능`합니다.

혹시라도 DispatcherServlet 을 여러개를 만드는 경우 `여러 DispatcherServlet 들이 서로 공용으로 써야하는 Bean 존재하는 경우를 커버`하기 위해서
`상속구조`를 만들 수 있게 되어있는 것입니다.

(Root) 웹 에플리케이션 내부에는 Web 과 관련된 Bean 들이 존재하지 않습니다.
이유는 `Service & Repository 는 다른 DispatcherServlet 에서도 공용으로 사용할 수 있는 Bean` 이라서 그렇습니다.

DispatcherServlet 에서 만드는 (서블릿) 웹 에플리케이션은 웹과 관련된 Bean들이 등록이 되어있습니다.
`Controller & ViewResolver & HandlerMapping 이러한 Bean들은 해당 DispatcherServlet 에서만 한정적`입니다.

# DispatcherServlet 동작 원리

> HelloController.class

~~~
@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public String hello() {
        return helloService.getName();
    }
}
~~~

HelloController.class 를 사용할 수 있도록 DispatcherServlet 사용하여 등록합니다.

Controller 는 DispatcherServlet 이 만드는 에플리케이션에 등록이 되어야 하며

> AppConfig.class

~~~
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(Controller.class))
public class AppConfig { }
~~~

(Root) 웹 에플리케이션 에서는 Controller 를 Bean 으로 등록하지 않겠다. 

> WebConfig.class

~~~
@Configuration
@ComponentScan(useDefaultFilters = false, includeFilters = @ComponentScan.Filter(Controller.class))
public class WebConfig { }
~~~

Controller 만 Bean 으로 등록하겠다.

> web.xml

~~~
  <context-param>
    <param-name>contextClass</param-name>
    <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
  </context-param>
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>me.whiteship.AppConfig</param-value>
  </context-param>
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

=================================================================================================== 

  <servlet>
    <servlet-name>app</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    
    WebConfig 설정을 사용해서 AnnotationConfigWebApplicationContext 에플리케이션을 만들도록 설정 contextClass 를 변경합니다.

    <init-param>
      <param-name>contextClass</param-name>
      <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </init-param>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>me.whiteship.WebConfig</param-value>
    </init-param>
  </servlet>

  app 서블릿이 "/app/*" 경로로 들어오는 모든 요청을 DispatcherServlet가 처리하겠다고 명시합니다.

  <servlet-mapping>
    <servlet-name>app</servlet-name>
    <url-pattern>/app/*</url-pattern>
  </servlet-mapping>
~~~

`Controller` 는 DispatcherServlet 에서 만들어주는 AnnotationConfigWebApplicationContext `내부에 WebConfig 으로 등록`이 되어 있습니다.
`DispatcherServlet` 는 현재 서블릿 컨텍스트에 들어있는 ContextLoaderListener 만들어준 `AnnotationConfigWebApplicationContext 를 부모로 사용`합니다.
부모로 사용한 `AnnotationConfigWebApplicationContext 는 AppConfig 를 통해서 만들어 집니다.`
`AppConfig` 는 Controller 이외의 모든 Bean 들이 들어있으니 `Service 가 등록`되어 있습니다.

"/app/*" `요청으로 들어오면` DispatcherServlet 이 요청을 처리할 `Controller 찾아서 디스패치` 해줍니다.

요약하면

- Servlet WebApplicationContext 
    - Controller 
    - ViewResolver 
    - HandlerMapping

- Root WebApplicationContext
    - Service
    - Repository

> Servlet WebApplicationContext -> Root WebApplicationContext

# DispatcherServlet

- DispatcherServlet 초기화
  - 다음의 특별한 타입의 빈들을 찾거나, 기본 전략에 해당하는 빈들을 등록한다.
  - HandlerMapping: 핸들러를 찾아주는 인터페이스
  - HandlerAdapter: 핸들러를 실행하는 인터페이스
  - HandlerExceptionResolver
  - ViewResolver
  - ...

- DispatcherServlet 동작 순서
  - 1. 요청을 분석한다. (로케일, 테마, 멀티파트 등)
  - 2. (핸들러 맵핑에게 위임하여) 요청을 처리할 핸들러를 찾는다.
  - 3. (등록되어 있는 핸들러 어댑터 중에) 해당 핸들러를 실행할 수 있는 “핸들러 어댑터”를 찾는다.
  - 4. 찾아낸 “핸들러 어댑터”를 사용해서 핸들러의 응답을 처리한다.
    - 핸들러의 리턴값을 보고 어떻게 처리할지 판단한다.
      - 뷰 이름에 해당하는 뷰를 찾아서 모델 데이터를 랜더링한다.
      - @ResponseEntity가 있다면 Converter를 사용해서 응답 본문을 만들고.
  - 5. (부가적으로) 예외가 발생했다면, 예외 처리 핸들러에 요청 처리를 위임한다.
  - 6. 최종적으로 응답을 보낸다.
- HandlerMapping
  - RequestMappingHandlerMapping
- HandlerAdapter
  - RequestMappingHandlerAdapter

> DispatcherServlet.class

~~~
첫째로 모든 요청은 적절한 HttpServletRequest, HttpServletResponse 객체를 매개변수로 받는 `doService()를 호출`하게 됩니다. 
doDispatch() 에게 위임 합니다.

디버그 > protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception { ... }

...
실제로 핸들러에게 요청을 전달하는 처리를 한다고 나와있습니다.

디버그 into > this.doDispatch(request, response);

... 

protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
  ...
  디버그 into > mappedHandler = this.getHandler(processedRequest);
    if (mappedHandler == null) {
        this.noHandlerFound(processedRequest, response);
        return;
    }
  }

  protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
    if (this.handlerMappings != null) {
      ...
      while(var2.hasNext()) {
          HandlerMapping mapping = (HandlerMapping)var2.next();

          HandlerExecutionChain 에서는 간단히 말해 Object 타입으로 전달되는 handler를 원래의 Handler 타입에 맞게 변환하여 그 Handler를 전달하는 등의 일을 하고있습니다. 

          HandlerExecutionChain handler = mapping.getHandler(request);
          if (handler != null) {
              return handler;
          }
      }
    }
  }

  HandlerAdapter ha = this.getHandlerAdapter(mappedHandler.getHandler());

  protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
    if (this.handlerAdapters != null) {
        Iterator var2 = this.handlerAdapters.iterator();

        while(var2.hasNext()) {
            HandlerAdapter adapter = (HandlerAdapter)var2.next();
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
    }
    ...
  }

  디버그 into > mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
}
~~~

doDispatch 메소드에서는 요청이 멀티파트인지, 로케일은 어떤것인지 등등 을 판단합니다.

getHandler() : processedRequest 요청을 처리할 수 있는 핸들러를 찾아오는 메소드입니다.
DispatcherServlet 이 들고있는 여러개의 핸들러 맵핑 중에서 RequestMappingHandlerMapping 사용하여 핸들러를 찾아옵니다.
핸들러를 찾은경우 getHandlerAdapter() 메소드로 핸들러를 실행시켜줍니다.

핸들러를 찾았고 핸들러를 실행시켜주는 어뎁터도 찾았습니다.

handle() 핸들러 어뎁터를 가지고 요청을 처리하는 메소드 

AbstractHandlerMethodAdapter -> RequestMappingHandlerAdapter

> RequestMappingHandlerAdapter.class

~~~
...
mav = this.invokeHandlerMethod(request, response, handlerMethod);
~~~

핸들러 메소드를 invoke 하게 됩니다.
즉 Java 리플렉션을 사용해서 HelloController 의 hello() 핸들러를 실행하게 됩니다.
handlerMethod 라는 객체 내부에는 해당 핸들러의 정보가 들어있습니다.

실행을 하면 결과같은 문자열로 반환됩니다.

- 요약

1. 사용자가 요청을 하게 되면 DispatcherServlet의 doService()가 호출됩니다.
2. doService()에서는 이 요청을 처리하기 위핸 Handler와 Adapter를 찾아 호출하기 위해 다시 doDispatch()를 호출합니다.
3. doDistpatch()에서는 HandlerMapping을 통해서 요청에 맞는 HandlerMethod를 찾습니다. 그 후 찾아낸 HandlerMethod를 호출할 수 있는 HandlerAdapter를 찾습니다.
4. HandlerAdpater에 의해 실질적으로 handlerMethod가 실행됩니다.
5. 마지막으로 ReturnValueHandler에 의해 handlerMethod의 실행 결과값을 적절한 Response로 생성하여 사용자에게 응답합니다.

## DispatcherServlet View 존재하는 경우

~~~
@Controller
public class SampleController {

    @GetMapping("/sample")
    public String sample() {
        return "WEB-INF/sample.jsp";
    }
}
~~~

이전의 ResponseBody 사용한 리턴이 아닌 문자열만 리턴입니다.

이경우는 모댈,View 값이 null 이 아닙니다.

> DispatcherServlet.class

~~~
디버그 > mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
~~~

mv : ModelAndView [view="/WEB-INF/sample.jsp"; model={}]

ModelAndView 값을 가지고있습니다.

이제 View의 객체를 탐색하여 모델객체에 넣어 리스폰스 응답해줍니다.

## 다른방식의 등록

> SimpleController.class

~~~
@org.springframework.stereotype.Controller("/simple")
public class SimpleController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws Exception {
        return new ModelAndView("/WEB-INF/simple.jsp");
    }
}
~~~

핸들러는 "org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping" 으로 실행됩니다.

어노테이션 기반의 핸들러가 아니라서 이경우 핸들러 어뎁터도 변경됩니다 simplecontrollerhandleradapter 으로 인해서 실행되게 됩니다.

# 커스텀 ViewResolver 등록

ViewResolver는 `사용자가 요청한 것에 대한 응답 view를 렌더링하는 역할`을 합니다. 

> DispatcherServlet.class

~~~
protected void initStrategies(ApplicationContext context) {
  ...
  this.initViewResolvers(context);
}

private void initViewResolvers(ApplicationContext context) {
  Map<String, ViewResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, ViewResolver.class, true, false);
  if (!matchingBeans.isEmpty()) {
      this.viewResolvers = new ArrayList(matchingBeans.values());
      AnnotationAwareOrderComparator.sort(this.viewResolvers);
  }
}
~~~

ViewResolver.class 정보를 찾아서 Bean 정보를 this.viewResolvers 목록에 넣어둡니다.

~~~
if (this.viewResolvers == null) {
    this.viewResolvers = this.getDefaultStrategies(context, ViewResolver.class);
}
~~~

하지만 직접 Bean 으로 등록한것이 없다면 기본전략을 가져옵니다.

initHandlerMappings 동일하게 진행됩니다.

~~~
private void initHandlerMappings(ApplicationContext context) {

    if (this.detectAllHandlerMappings) {
        Map<String, HandlerMapping> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
        if (!matchingBeans.isEmpty()) {
            this.handlerMappings = new ArrayList(matchingBeans.values());
            AnnotationAwareOrderComparator.sort(this.handlerMappings);
        }
    } else {
      ...
      HandlerMapping hm = (HandlerMapping)context.getBean("handlerMapping", HandlerMapping.class);
    }
}
~~~

HandlerMapping.class 타입의 Bean을 전부 찾아서 this.handlerMappings 목록에 넣습니다.

여기서 조건문인 detectAllHandlerMappings 값은 전부다 찾아서 쓸건지의 플레그가 존재하며 기본값은 true 입니다.

만약 false 인경우 특정 Bean 만 탐색하도록 설정할 수도 있습니다.
성능 최적화를 위해서 사용할 수도 있습니다.

## ViewResolver Bean 등록해서 사용해보기

~~~
@Configuration
public class WebConfig {

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }
}
~~~

Spring Boot에서 view이름만 적어주어도 알맞은 view를 찾아가는 이유는 suffix, prefix 설정때문 입니다.
InternalResourceViewResolver 에서는 요청에 맞는 View를 렌더링하여 반환해주는 역할을 한다고했습니다. 
`Prefix는 렌더링시 handler에서 반환하는 문자열의 앞에 Resolver가 자동으로 붙혀줄 문자열`을 의미합니다. 
`Suffix는 뒷쪽에 붙는 문자열`입니다. 
따라서 이처럼 설정하게 되면 단순히 handler에서 view 이름만을 반환하면 ViewResolver에서 view의 위치와 확장자를 연결하여 view를 렌더링하게 됩니다.

InternalResourceViewResolver 설정을 하면 Controller 에서 return 값을 간결하게 사용할 수 있습니다.
return new ModelAndView("/WEB-INF/simple.jsp"); -> return new ModelAndView("simple");

~~~
private void initViewResolvers(ApplicationContext context) {
디버그 > this.viewResolvers = null;
~~~

this.viewResolvers 저장된 값을 보면 prefix, suffix 설정된 값이 정상적으로 적용되는 것을 확인 할 수 있습니다.

viewResolvers 값이 존재하므로 기본 viewResolver 는 적용되지 않습니다.

# 스프링 MVC 구성 요소

- DispatcherSerlvet의 기본 전략
  - DispatcherServlet.properties

- MultipartResolver
  - `파일 업로드 요청 처리에 필요한 인터페이스`
  - HttpServletRequest를 MultipartHttpServletRequest로 변환해주어 요청이 담고 있는 File을 꺼낼 수 있는 API 제공.

- LocaleResolver
  - `클라이언트의 위치(Locale) 정보를 파악하는 인터페이스` 요청이 어느지역에서 온것인지 지역정보를 확인할 때 사용합니다.
  - 기본 전략은 요청의 accept-language를 보고 판단.
  - 요청이 DispatcherServlet 내부에 들어왔을 때 분석하는 단계에서 사용됩니다. 

- ThemeResolver
  - 애플리케이션에 `설정된 테마를 파악하고 변경할 수 있는 인터페이스` css 설정을 감지하고 변경하는 뜻
  - 참고: https://memorynotfound.com/spring-mvc-theme-switcher-example 

- HandlerMapping
  - `요청을 처리할 핸들러를 찾는 인터페이스`
  - DispatcherServlet.properties
    - BeanNameUrlHandlerMapping : @Controller("/simple") Bean 이름 정보로 핸들러를 찾아주는 맵핑
    - RequestMappingHandlerMapping : 어노테이션 기반의 정보로 핸들러를 찾아주는 맵핑

- HandlerAdapter
  - `HandlerMapping이 찾아낸 “핸들러”를 처리하는 인터페이스`
  - 스프링 MVC 확장력의 핵심

- HandlerExceptionResolver
  - `요청 처리 중에 발생한 에러 처리하는 인터페이스`

- RequestToViewNameTranslator
  - `핸들러에서 뷰 이름을 명시적으로 리턴하지 않은 경우`, 요청을 기반으로 뷰 이름을 판단하는 인터페이스

- ViewResolver
  - handler에서 반환하는 View 이름(String)에 해당하는 View를 찾아내는 인터페이스 입니다.
  - 개발자가 별도의 Bean을 등록하지 않는다면, 기본전략으로 InternalResourceViewResolver 가 사용이됩니다. InternalResourceViewResolver 는 기본적으로 JSP를 지원하기 때문에 지금까지 JSP를 사용하여 반환할 수 있었던 것입니다.

- FlashMapManager
  - FlashMap 인스턴스를 가져오고 저장하는 인터페이스
  - FlashMap은 주로 리다이렉션을 사용할 때 요청 매개변수를 사용하지 않고 데이터를 전달하고 정리할 때 사용한다.
  - 필요에 의해 Redirect를 할 때에 데이터를 손쉽게 전달할때 사용합니다.
  - 즉, Redirect시 데이터를 전달할때에는 파라미터를 이용해 전달합니다 (http://example?a=1&b=2). 이때, url이 굉장히 복잡해지고, url의 길이제한에 걸릴 수도 있습니다. 즉 이런 경우에 FlashMap을 복원한다면 redirect 될때 단한번만 값을 유지하도록 할 수 있습니다.

# 스프링 MVC 동작 원리

결국엔 (굉장히 복잡한) 서블릿. = DispatcherServlet

- DispatcherServlet 초기화
  - 1. 특정 타입에 해당하는 빈을 찾는다.
  - 2. 없으면 기본 전략을 사용한다. (DispatcherServlet.properties)

- 스프링 부트 사용하지 않는 스프링 MVC
  - 서블릿 컨네이너(ex, 톰캣)에 등록한 웹 애플리케이션(WAR)에 DispatcherServlet을 등록한다.
    - web.xml에 서블릿 등록
    - 또는 WebApplicationInitializer에 자바 코드로 서블릿 등록 (스프링 3.1+, 서블릿 3.0+)
  - 세부 구성 요소는 빈 설정하기 나름.

- 스프링 부트를 사용하는 스프링 MVC
  - 자바 애플리케이션에 내장 톰캣을 만들고 그 안에 DispatcherServlet을 등록한다.
    - 스프링 부트 자동 설정이 자동으로 해줌.
  - 스프링 부트의 주관에 따라 여러 인터페이스 구현체를 빈으로 등록한다.

## web xml 설정없이 DispatcherServlet 

web.xml 설정없이 웹 애플리케이션 만들기

WebApplicationInitializer 인터페이스를 활용합니다.

> WebApplication.class

~~~
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
~~~

WebApplicationInitializer 를 구현합니다.  
이 인터페이스를 구현하면 `onStartup() 메소드`를 구현합니다. 
`WebApplication이 초기화 되는 시점에 호출`되기 때문에 마치 `web.xml의 설정파일 처럼 사용`할 수 있습니다. 
그 때문에, 매개변수로 주어지는 servletContext에 필요한 `servletMapping,listener, Filter 들을 등록`할 수 있습니다.

우선 DispatcherServlet 을 등록하기 전에 `DispatcherServlet에서 사용할 IoCContainer를 생성하고 설정`합니다.
`Annotation 기반의 설정`을 할 것이기 때문에 구현체인 `AnnotationConfigApplicationContext를 사용`하겠습니다. 
그 후 context.register() 메소드를 이용해 이전에 만들어 두었던 `WebConfig 클래스를 등록`해주고, 마지막으로 refresh()를 해줍니다. 
추가적으로 IoC Container(ApplicationContext)의 `setServletContext() 메소드를 이용해 ServletContext를 등록`해주어야 하는데, 
이는 종종 `IoC Container에서 ServletContext를 참조하는 코드가 있기 때문`입니다.

WebConfig는 web.xml 에 bean설정들을 하지 않고 또, 
그 bean설정을 한 파일의 위치를 web.xml에 작성하지 않도록하기 위해서, 
`@Configuration 을 이용해 Bean설정 파일임을 알려주고,`
@ComponentScan 을 이용해 `이 클래스가 위치한 곳을 기반으로 Streotype 의 어노테이션을 가지고 있는 모든 Bean을 자동으로 scan하여 IoC Container에 등록`해주도록 했습니다.

생성한 IoC Container 를 매개변수로하여, `DispatcherServlet 을 생성`합니다. 
그 후 onStartup() 메소드의 인자로 전달된 ServletContext 객체의 `addServlet() 메소드를 이용해, DispatcherServlet을 등록`합니다.

addServlet() 의 반환값으로 전달되는 ServletRegistration.Dynamic 객체를 이용해 addMapping() 을 호출하여 DispatcherServlet에 mapping될 url을 지정해주면 됩니다.

# @EnableWebMvc

애노테이션 기반 스프링 MVC를 사용할 때 편리한 웹 MVC 기본 설정

~~~
@Import({DelegatingWebMvcConfiguration.class})
public @interface EnableWebMvc { }
~~~

> WebConfig.class

~~~
@Configuration
@EnableWebMvc
public class WebConfig {

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
        handlerMapping.setInterceptors();
        return handlerMapping;
    }
}
~~~

위 처럼 직접 `WebConfig Class에 Bean을 직접 등록하여 DispatcherServlet 이 초기화` 되도록 할 수도 있었습니다. 
하지만 위의 방법은 `Low레벨의 설정방법`입니다. 
SpringBoot뿐만 아니라 Spring MVC에서도 위와 같이 `설정하는 경우는 없습니다.`

SpringMVC 에서 `@EnableWebMvc 어노테이션을 사용하여 Bean 을 등록`합니다.
@EnableWebMvc는 `어노테이션 기반의 SpringMvc를 구성할때 필요한 Bean설정들을 자동으로 해주는 어노테이션`입니다. 
또한 기본적으로 등록해주는 Bean들 이외에 `추가적으로 개발자가 필요로하는 Bean들을 등록을 손쉽게 할 수 있도록 도와줍니다.`

## 디버깅

- HandlerMapping

`기본 설정시에는 RequestMappingHandlerMaping 객체의 우선순위가 더 낮기때문`에 아래에 있었지만, 
Bean 직접등록하여 사용하는 설정이 아닌 `어노테이션 기반을 돕는 설정이기 때문에` RequestMappingHandlerMapping이 위로 올라온 것을 볼 수 있습니다.
각 `맵핑의 Order 값`을 보면 순서를 직관적으로 볼수 있습니다.

추가적으로 `Interceptor 도 자동으로 등록`되는 것을 볼수 있습니다. `(기본설정의 경우 Interceptor가 아예 없습니다.)`

이러한 기본설정의 위치

> EnableWebMvc.annotation > DelegatingWebMvcConfiguration.class > WebMvcConfigurationSupport.class

~~~
...
  @Bean
  public RequestMappingHandlerMapping requestMappingHandlerMapping() {
    RequestMappingHandlerMapping mapping = this.createRequestMappingHandlerMapping();
    mapping.setOrder(0);
    mapping.setInterceptors(this.getInterceptors(conversionService, resourceUrlProvider));
    mapping.setContentNegotiationManager(contentNegotiationManager);
    mapping.setCorsConfigurations(this.getCorsConfigurations());
    ...
  }
...
~~~

- HandlerAdapter

HandlerAdapter의 messageConverters의 경우 7개의 converter가 자동으로 등록되어 있는 것을 볼 수 있습니다. 
추가적으로 Jackson 의존성이 추가되어있다면 requestbody, responsebody에 json형식으로 컨버팅이 가능한 Converter가 자동으로 등록이 되도록 되어있습니다. 

> EnableWebMvc.annotation > DelegatingWebMvcConfiguration.class > WebMvcConfigurationSupport.class

~~~
...
  static {
      ClassLoader classLoader = WebMvcConfigurationSupport.class.getClassLoader();
      romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", classLoader);
      jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", classLoader);
      jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader);
      jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);
      jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
      jackson2CborPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.cbor.CBORFactory", classLoader);
      gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
      jsonbPresent = ClassUtils.isPresent("javax.json.bind.Jsonb", classLoader);
  }
...
~~~

class에 가면 static 필드에서 `classpath를 탐색해 해당 클래스들이 존재하는지를 보고 존재의 진위여부를 boolean값으로 저장`합니다. 
이 boolean 값은 앞으로 설명드릴 `messageConverter 등록 과정에서 사용`됩니다.

~~~
  @Bean
  public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
    ...
    adapter.setMessageConverters(this.getMessageConverters());
    ...
  }
~~~

RequestMappingHandlerAdapter를 Bean으로 등록하는 메소드 입니다. 
해당 메소드에서는 RequestMappingHandlerAdapter 객체를 생성한 뒤 각각의 필요한 필드 값들을 또다른 메소드를 통해 등록하고 있습니다. 
`messageConverter 를 등록하기 위해 사용되는 getMessageConverters() 메소드`를 보겠습니다.

~~~
  protected final List<HttpMessageConverter<?>> getMessageConverters() {
      if (this.messageConverters == null) {
          this.messageConverters = new ArrayList();
          this.configureMessageConverters(this.messageConverters);

          if (this.messageConverters.isEmpty()) {
              this.addDefaultHttpMessageConverters(this.messageConverters);
          }

          this.extendMessageConverters(this.messageConverters);
      }

      return this.messageConverters;
  }
~~~

getMessageConverters() 메소드에서는 `개발자가 따로 registry를 통해 등록한 MessageConverter 가 존재하는지를 파악`합니다. 
`만약 없다면 addDefaultHttpMessageConverters() 를 다시 호출`합니다.

~~~
 protected final void addDefaultHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
    if (jackson2XmlPresent) {
      ...
    } else if (jaxb2Present) {
      ...
    }

    if (jackson2Present) {
      ...
    } else if (gsonPresent) {
      ...
    } else if (jsonbPresent) {
      ...
    }
    ...
 }
~~~

바로 여기서 맨 처음 static 필드에서 초기화 했던 boolean 값들을 이용하게 됩니다. 
여기서 각각의 Class들이 `classpath에 존재하는지를 파악한 뒤 만약 존재한다면 그에 해당하는 MessageConverter들을 자동으로 등록`하게 됩니다.

# @EnableWebMvc 어노테이션 사용시 설정 추가 방법 (WebMvcConfigurer Interface)

Spring MVC 가 Boot 없이 동작되는 방법

WebMvcConfigurer Interface 는 @EnableWebMvc 어노테이션에서 제공하는 Bean을 커스터마이징(설정)할 수 있는 기능을 제공하는 인터페이스 입니다.

확장을 위해 WebMvcConfigurer를 구현합니다. 
이때 확장을 필요로하는 메소드만을 구현하여 확장해주면 됩니다. 
인터페이스를 사용했지만 abstract 클래스처럼 사용할 수 있는 이유는 각각의 메소드를 default로 작성했기 때문입니다.

~~~
@Configuration
@ComponentScan
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
      registry.jsp("/WEB-INF/", ".jsp");
  }
}
~~~

# 스프링 부트의 스프링 MVC 설정

Spring Boot 프로젝트 "demo-spring-mvc" 

> DispatcherServlet.class

~~~
디버그 > protected void doService() {

}
~~~

DispatcherServlet 의 this 내부에 어떠한 handlerMappings, handlerAdapters 들을 가지고있는지 확인합니다.

- handlerMappings 
  - SimpleUrlHandlerMapping
    - beanName 으로 resourceHandlerMapping 등록되어 있으며 해당 핸들러로 인해서 정적 리소스 지원이 가능합니다. (/main/resources/static/...)
  - WelcomePageHandlerMapping
    - 리소스 지원을 해주는 핸들러로 index mapping 을 해줍니다.

- handlerAdapters
  - RequestMappingHandlerAdapter
    - 실제 요청을 처리하는 핸들러를 실행해줄 수 있는 어뎁터

- ViewResolvers
  - ContentNegotiatingViewResolver
    - ContentNegotiatingViewResolver 이외의 모든 ViewResolver 들을 사용합니다.
    - 직접 View 이름에 해당하는 View 를 찾아주는게 아니라 다른 ViewResolver 들에게 위임합니다.
    - ContentNegotiatingViewResolver -> ViewResolvers 내부에 다른 ViewResolver 들을 참조하는 것을 알 수 있습니다.

이러한 설정들을 불러오는 위치

> spring-boot-autoconfigure/spring.factories

~~~
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
...
org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration,\
...
~~~

EnableAutoConfiguration 해당하는 밑의 모든 자동설정 파일이 조건에 따라서 자동설정 됩니다.

> WebMvcAutoConfiguration.class

~~~
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
		ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {
  ...
}
~~~

- ConditionalOnWebApplication : 웹 에플리케이션 타입이 서블릿일때만 실행됩니다.
- ConditionalOnClass : 등록된 클래스가 존재하면 실행됩니다.
- ConditionalOnMissingBean : 선언된 클래스가 없는경우 실행됩니다.
  - WebMvcConfigurationSupport 는 DelegatingWebMvcConfiguration 의 부모입니다.
  - Spring Boot 가 지원하는 MVC 자동설정을 사용하지 않으려면?
  - @Configuration + @EnableWebMvc + Imlements WebMvcConfigurer 를 사용하면 커스텀 가능합니다.

# 스프링 부트 JSP

- “If possible, JSPs should be avoided. There are several known limitations when using
them with embedded servlet containers.”
  - https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-spring-mvc-template-engines

- 제약 사항
  - JAR 프로젝트로 만들 수 없음, WAR 프로젝트로 만들어야 함
  - Java -JAR로 실행할 수는 있지만 “실행가능한 JAR 파일”은 지원하지 않음
  - 언더토우(JBoss에서 만든 서블릿 컨테이너)는 JSP를 지원하지 않음
  - Whitelabel 에러 페이지를 error.jsp로 오버라이딩 할 수 없음.

- 참고
  - https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-jsp-limitations
  - https://github.com/spring-projects/spring-boot/tree/v2.1.1.RELEASE/spring-boot-samples/spring-boot-sample-web-jsp (샘플 프로젝트)

War 프로젝트로 패키징을 만들면 SpringBootServletInitializer 를 상속받는 클레스가 하나 더 생성되어 있습니다.
SpringBootServletInitializer는 웹 에플리케이션을 패키징 한다음에 독립적인 Jar 파일로 실행은 가능합니다.
하지만 War 파일이기 때문에 웹서버(톰켓)에 배포할 수도 있습니다.

- 독립적인 War 파일로 실행을 할 때는 기본 @SpringBootApplication 클래스로 실행을 합니다.
- 톰켓이나 서블릿 엔진에 배포를 하는 형태인 경우 SpringBootServletInitializer 으로 실행을 합니다.

> Event.class

~~~
public class Event {
    private String name;
    private LocalDateTime starts;

    // Getter, Setter
}
~~~

> EventController.class

~~~
@Controller
public class EventController {

    @GetMapping("/events")
    public String getEvents(Model model) {
        Event event1 = new Event();
        event1.setName("스프링 MVC 1");
        event1.setStarts(LocalDateTime.of(2020, 1, 1, 1, 1));

        Event event2 = new Event();
        event2.setName("스프링 MVC 2");
        event2.setStarts(LocalDateTime.of(2020, 2, 2, 2, 2));

        List<Event> eventList = List.of(event1, event2);

        model.addAttribute("events", eventList);
        model.addAttribute("message", "Hello~!");

        return "events/list";
    }
}
~~~

> main/webapp/WEB-INF/jsp/events/list.jsp

~~~
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>이벤트 목록</h1>
    <h4>${message}</h4>
    <table>
        <tr>
            <th>이름</th>
            <th>시작</th>
        </tr>
        <c:forEach items="${events}" var="event">
            <tr>
                <td>${event.name}</td>
                <td>${event.starts}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
~~~

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 추가하여 jstl 사용할 수 있도록 합니다.

> application.properties

~~~
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
~~~

mvc view 기본설정을 해줍니다.

War 패키징 후 명령어 java -jar 로 실행해주면 동일하게 실행됩니다.

## WAR 파일 배포하기

java -jar를 사용해서 실행한 경우

- SpringApplication.run 사용하기

(Embedded Tomcat) DispatcherServlet -> Spring IOC Container

- SpringBootServletInitializer (WebApplicationInitializer) 사용하기

Servlet Container (Tomcat, Jetty) -> Web Application aRchive (WAR) -> DispatcherServlet -> Spring IOC Container

# 도메인 클래스 컨버터

- 스프링 데이터 JPA는 스프링 MVC용 도메인 클래스 컨버터를 제공합니다.
- 도메인 클래스 컨버터
  - 스프링 데이터 JPA가 제공하는 Repository를 사용해서 ID에 해당하는 엔티티를 읽어옵니다.

> SampleControllerTest.class

~~~
@RunWith(SpringRunner.class)
@WebMvcTest
public class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() throws Exception {
        mockMvc
                .perform(
                        get("/hello")
                        .param("id", "1")
                )
                .andExpect(content().string("hello"))
                .andDo(print());
    }
}
~~~

param 의 id 값에 해당하는 Person 객체의 이름이 출력되면 됩니다.

> SampleController.class

~~~
@RestController
public class SampleController {

    @GetMapping("/hello")
    public String hello(@RequestParam("id") Person person) {

        return "hello" + person.getName();
    }
}
~~~

> PersonRepository.class

~~~
public interface PersonRepository extends JpaRepository<Person, Long> { }
~~~

# 핸들러 인터셉터

- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/WebMvcConfigurerhtml#addInterceptors-org.springframework.web.servlet.config.annotation.InterceptorRegistry-

- HandlerInterceptor
  - 핸들러 맵핑에 설정할 수 있는 인터셉터
  - 핸들러를 실행하기 전, 후(아직 랜더링 전) 그리고 완료(랜더링까지 끝난 이후) 시점에 부가 작업을 하고 싶은 경우에 사용할 수 있다.
  - 여러 핸들러에서 반복적으로 사용하는 코드를 줄이고 싶을 때 사용할 수 있다.
    - 로깅, 인증 체크, Locale 변경 등...

- boolean preHandle(request, response, handler)
  - 핸들러 실행하기 전에 호출 됨
  - “핸들러"에 대한 정보를 사용할 수 있기 때문에 서블릿 필터에 비해 보다 세밀한 로직을 구현할 수 있다.
  - 리턴값으로 계속 다음 인터셉터 또는 핸들러로 요청,응답을 전달할지(true) 응답 처리가 이곳에서 끝났는지(false) 알린다.

- void postHandle(request, response, modelAndView)
  - 핸들러 실행이 끝나고 아직 뷰를 랜더링 하기 이전에 호출 됨
  - “뷰"에 전달할 추가적이거나 여러 핸들러에 공통적인 모델 정보를 담는데 사용할 수도 있다.
  - 이 메소드는 인터셉터 역순으로 호출된다.
  - 비동기적인 요청 처리 시에는 호출되지 않는다.

- void afterCompletion(request, response, handler, ex)
  - 요청 처리가 완전히 끝난 뒤(뷰 랜더링 끝난 뒤)에 호출 됨
  - preHandler에서 true를 리턴한 경우에만 호출 됨
  - 이 메소드는 인터셉터 역순으로 호출된다.
  - 비동기적인 요청 처리 시에는 호출되지 않는다.

- vs 서블릿 필터
  - 서블릿 보다 구체적인 처리가 가능하다.
  - 서블릿은 보다 일반적인 용도의 기능을 구현하는데 사용하는게 좋다.

- 참고:
  - https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/HandlerInterceptor.html
  - https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/AsyncHandlerInterceptor.html

## 핸들러 인터셉터 구현

> GreetingInterceptor.class

~~~
public class GreetingInterceptor implements HandlerInterceptor {

    /**
     * return 값을 true 하여 다음으로 요청 처리를 하도록 설정합니다.
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        System.out.println("preHandle 1");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle 1");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion 1");
    }
}
~~~

> AnotherInterceptor.class

~~~
public class AnotherInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        System.out.println("preHandle 2");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle 2");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion 2");
    }
}
~~~

인터셉터 설정 등록은 WebMvcConfigurer 에서 가능합니다.

> WebConfig.class

~~~
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Order 순서값을 주지않으면 등록 순서대로 진행합니다.
     * addPathPatterns : 특정 패턴에만 적용하는 설정메소드
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GreetingInterceptor());
        registry.addInterceptor(new AnotherInterceptor())
                .order(-1)
                .addPathPatterns("/hi");
    }
}
~~~

# 리소스 핸들러

- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/WebMvcConfigurerhtml#addResourceHandlers-org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry-

- 이미지, 자바스크립트, CSS 그리고 HTML 파일과 같은 `정적인 리소스를 처리하는 핸들러 등록`하는 방법

- 디폴트(Default) 서블릿
  - 서블릿 컨테이너가 기본으로 제공하는 서블릿으로 정적인 리소스를 처리할 때 사용한다.
  - https://tomcat.apache.org/tomcat-9.0-doc/default-servlet.html

- 스프링 MVC 리소스 핸들러 맵핑 등록
  - 가장 낮은 우선 순위로 등록.
    - 다른 핸들러 맵핑이 “/” 이하 요청을 처리하도록 허용하고
    - 최종적으로 리소스 핸들러가 처리하도록.
- DefaultServletHandlerConfigurer

- 리소스 핸들러 설정
  - 어떤 요청 패턴을 지원할 것인가
  - 어디서 리소스를 찾을 것인가
  - 캐싱
  - ResourceResolver: 요청에 해당하는 리소스를 찾는 전략
    - 캐싱, 인코딩(gzip, brotli), WebJar, ...
  - ResourceTransformer: 응답으로 보낼 리소스를 수정하는 전략
    - 캐싱, CSS 링크, HTML5 AppCache, ...

> WebConfig.class

~~~
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/mobile/**")
              .addResourceLocations("classpath:/mobile/")
              .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
              .resourceChain(true);;
  }
}
~~~

return 하는 리소스들은 기본적으로 캐쉬와 관련된 Header 와 같이 응답 Header 에 추가가 되고
응답은 리소스가 변경되지 않았다면 10분동안 캐싱이 됩니다.

resourceChain : 캐쉬를 사용할것인지 아닌지를 설정하는 메소드 (개발중인지 운영중인지)

> Test.class

~~~
@Test
public void helloStatic() throws Exception {
    mockMvc.perform(get("/mobile/index.html"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("mobile")))
            .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
            .andDo(print());
}
~~~

- 스프링 부트
  - `기본 정적 리소스 핸들러와 캐싱 제공` 

- 참고
  - https://www.slideshare.net/rstoya05/resource-handling-spring-framework-41

# HTTP 메시지 컨버터

- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/WebMvcConfigurer.html#configureMessageConverters-java.util.List-
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/WebMvcConfigurer.html#extendMessageConverters-java.util.List-

- HTTP 메시지 컨버터
  - 요청 본문에서 메시지를 읽어들이거나(@RequestBody), 응답 본문에 메시지를 작성할 때(@ResponseBody) 사용한다.

> Controller.class

~~~
@RestController
public class SampleController {
  @GetMapping("/message")
  // @ResponseBody
  public String message(@RequestBody String body) {
      return body;
  }
}
~~~

> Test.class

~~~
@Test
public void stringMessage() throws Exception {
    mockMvc.perform(get("/message").content("hello"))
            .andExpect(status().isOk())
            .andExpect(content().string("hello"))
            .andDo(print());
}
~~~

본문으로 받은 hello 값을 문자열로 hello를 반환해줬습니다.

- @RequestBody : 어노테이션을 사용하면 본문에 들어있는 메세지를 Http Message 컨버터를 사용해서 컨버전을 합니다.
- @ResponseBody : 어노테이션이 붙어있으면 return 값을 응답의 본문으로 넣어줍니다.
하지만 @RestContoller 어노테이션이 상위에 붙어있기 때문에 @ResponseBody 값은 디폴트값입니다. 생략 가능

본문의 문자열 JSON을 객체로 변환하거나 문자열로 받거나 가능합니다.

- 기본 HTTP 메시지 컨버터
  - 바이트 배열 컨버터
  - 문자열 컨버터
  - Resource 컨버터
  - Form 컨버터 (폼 데이터 to/from MultiValueMap<String, String>)
  - (JAXB2 컨버터)
  - (Jackson2 컨버터)
  - (Jackson 컨버터)
  - (Gson 컨버터)
  - (Atom 컨버터)
  - (RSS 컨버터)
  - ...

- 설정 방법
  - 기본으로 등록해주는 컨버터에 새로운 컨버터 추가하기: extendMessageConverters
  - 기본으로 등록해주는 컨버터는 다 무시하고 새로 컨버터 설정하기: configureMessageConverters
  - 의존성 추가로 컨버터 등록하기 (추천)
    - 메이븐 또는 그래들 설정에 의존성을 추가하면 그에 따른 컨버터가 자동으로 등록 된다.
    - WebMvcConfigurationSupport
    - (이 기능 자체는 스프링 프레임워크의 기능임, 스프링 부트 아님.)

- 참고
  - https://www.baeldung.com/spring-httpmessageconverter-rest

## JSON

- 스프링 부트를 사용하지 않는 경우
  - 사용하고 싶은 JSON 라이브러리를 의존성으로 추가
  - GSON
  - JacksonJSON
  - JacksonJSON 2

- 스프링 부트를 사용하는 경우
  - 기본적으로 JacksonJSON 2가 의존성에 들어있다.
  - 즉, JSON용 HTTP 메시지 컨버터가 기본으로 등록되어 있다.

- 참고
  - JSON path 문법
  - https://github.com/json-path/JsonPath
  - http://jsonpath.com/

> Controller.class

~~~
@GetMapping("/jsonMessage")
public Person jsonMessage(@RequestBody Person person) {
    return person;
}
~~~

입력값도 JSON 반환하는 값도 JSON

> Test.class

~~~
@Test
public void jsonMessage() throws Exception {
    Person person = new Person();
    person.setId(2020L);
    person.setName("jjunpro");

    String personJson = objectMapper.writeValueAsString(person);

    mockMvc.perform(get("/message")
            .content(personJson)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("hello"))
            .andExpect(jsonPath("$.id").value(2020))
            .andDo(print());
}
~~~

contentType : 사용자가 본문에 보내는 정보가 어떠한 타입인지 서버에 알려주는 정보
accept : 요청의 대한 응답으로 어떠한 타입을 원하는지 알려주는 정보
jsonPath : Json 정보와 일치하는지 검사합니다.

## XML

- OXM(Object-XML Mapper) 라이브러리 중에 스프링이 지원하는 의존성 추가
  - JacksonXML
  - JAXB

- 스프링 부트를 사용하는 경우
  - 기본으로 XML 의존성 추가해주지 않음.

- JAXB 의존성 추가

~~~
<dependency>
<groupId>javax.xml.bind</groupId>
<artifactId>jaxb-api</artifactId>
</dependency>
<dependency>
<groupId>org.glassfish.jaxb</groupId>
<artifactId>jaxb-runtime</artifactId>
</dependency>
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-oxm</artifactId>
<version>${spring-framework.version}</version>
</dependency>
~~~

- Marshaller 등록

~~~
@Bean
public Jaxb2Marshaller marshaller() {
  Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
  jaxb2Marshaller.setPackagesToScan(Event.class.getPackageName());
  return jaxb2Marshaller;
}
~~~

~~~
@XmlRootElement
@Entity
public class Person {
  ...
}
~~~

- 도메인 클래스에 @XmlRootElement 애노테이션 추가
- 참고
  - Xpath 문법
  - https://www.w3schools.com/xml/xpath_syntax.asp
  - https://www.freeformatter.com/xpath-tester.html

> Controller.class

~~~
@GetMapping("/jsonMessage")
public Person jsonMessage(@RequestBody Person person) {
    return person;
}
~~~

입력값도 JSON 반환하는 값도 JSON

> Test.class

~~~
@Test
public void xmlMessage() throws Exception {
    Person person = new Person();
    person.setId(2020L);
    person.setName("jjunpro");

    StringWriter stringWriter = new StringWriter();
    StreamResult streamResult = new StreamResult(stringWriter);
    marshaller.marshal(person, streamResult);
    String xmlString = stringWriter.toString();

    mockMvc.perform(get("/jsonMessage")
            .content(xmlString)
            .contentType(MediaType.APPLICATION_XML)
            .accept(MediaType.APPLICATION_XML))
            .andExpect(status().isOk())
            .andExpect(xpath("person/id").string("2020"))
            .andDo(print());
}
~~~

# 그밖에 WebMvcConfigurer 설정

- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/WebMvcConfigurer.html

- CORS 설정
  - Cross Origin 요청 처리 설정
  - 같은 도메인에서 온 요청이 아니더라도 처리를 허용하고 싶다면 설정한다.

- 리턴 값 핸들러 설정
  - 스프링 MVC가 제공하는 기본 리턴 값 핸들러 이외에 리턴 핸들러를 추가하고 싶을 때 설정한다.

- 아큐먼트 리졸버 설정
  - 스프링 MVC가 제공하는 기본 아규먼트 리졸버 이외에 커스텀한 아규먼트 리졸버를 추가하고 싶을 때 설정한다.

- 뷰 컨트롤러
  - 단순하게 요청 URL을 특정 뷰로 연결하고 싶을 때 사용할 수 있다.

- 비동기 설정
  - 비동기 요청 처리에 사용할 타임아웃이나 TaskExecutor를 설정할 수 있다.

- 뷰 리졸버 설정
  - 핸들러에서 리턴하는 뷰 이름에 해당하는 문자열을 View 인스턴스로 바꿔줄 뷰 리졸버를 설정한다.

- Content Negotiation 설정
  - 요청 본문 또는 응답 본문을 어떤 (MIME) 타입으로 보내야 하는지 결정하는 전략을 설정한다.

# 요청 맵핑하기 HTTP Method

- HTTP Method
  - GET, POST, PUT, PATCH, DELETE, ...

- GET 요청
  - 클라이언트가 서버의 리소스를 요청할 때 사용한다.
  - 캐싱 할 수 있다. (조건적인 GET으로 바뀔 수 있다.)
  - 브라우저 기록에 남는다.
  - 북마크 할 수 있다.
  - 민감한 데이터를 보낼 때 사용하지 말 것. (URL에 다 보이니까)
  - idempotent

- POST 요청
  - 클라이언트가 서버의 리소스를 수정하거나 새로 만들 때 사용한다.
  - 서버에 보내는 데이터를 POST 요청 본문에 담는다.
  - 캐시할 수 없다.
  - 브라우저 기록에 남지 않는다.
  - 북마크 할 수 없다.
  - 데이터 길이 제한이 없다.

- PUT 요청
  - URI에 해당하는 데이터를 새로 만들거나 수정할 때 사용한다.
  - POST와 다른 점은 “URI”에 대한 의미가 다르다.
    - POST의 URI는 보내는 데이터를 처리할 리소스를 지칭하며
    - PUT의 URI는 보내는 데이터에 해당하는 리소스를 지칭한다.
  - Idempotent

- PATCH 요청
  - PUT과 비슷하지만, 기존 엔티티와 새 데이터의 차이점만 보낸다는 차이가 있다.
  - Idempotent

- DELETE 요청
  - URI에 해당하는 리소스를 삭제할 때 사용한다.
  - Idempotent

- 스프링 웹 MVC에서 HTTP method 맵핑하기
  - @RequestMapping(method=RequestMethod.GET)
  - @RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
  - @GetMapping, @PostMapping, ...

- 참고
  - https://www.w3schools.com/tags/ref_httpmethods.asp
  - https://tools.ietf.org/html/rfc2616#section-9.3
  - https://tools.ietf.org/html/rfc2068

# 요청 맵핑하기 URI 패턴

- URI, URL, URN 햇갈린다
  - https://stackoverflow.com/questions/176264/what-is-the-difference-between-a-uri-a-url-and-a-urn

- 요청 식별자로 맵핑하기
  - @RequestMapping은 다음의 패턴을 지원합니다.
  - ?: 한 글자 (“/author/???” => “/author/123”)
  - *: 여러 글자 (“/author/*” => “/author/keesun”)
  - ** : 여러 패스 (“/author/** => “/author/keesun/book”)

~~~
@GetMapping({"/hello", "/hi"})
@GetMapping("/hello/?")
~~~

- 클래스에 선언한 @RequestMapping과 조합
  - 클래스에 선언한 URI 패턴뒤에 이어 붙여서 맵핑합니다.

- 정규 표현식으로 맵핑할 수도 있습니다.
  - /{name:정규식}

~~~
@GetMapping("/{name:[a-z]+}")
@ResponseBody
public String hello(@PathVariable String name) {
    return "hello";
}
~~~

- 패턴이 중복되는 경우에는?
  - 가장 구체적으로 맵핑되는 핸들러를 선택합니다.

~~~
@Controller
@RequestMapping("/hello")
public class SampleController {

    @GetMapping("/jjunpro")
    @ResponseBody
    public String hello() {
        return "hello";
    }

    @GetMapping("/**")
    @RequestMapping
    public String helloAll() {
        return "helloAll";
    }
}

@Test
public void hello() throws Exception {
    mockMvc.perform(get("/hello/jjunpro"))
            .andExpect(status().isOk())
            .andExpect(content().string("hello"))
            .andExpect(handler().handlerType(SampleController.class))
            .andExpect(handler().methodName("hello"))
            .andDo(print());
}
~~~

- URI 확장자 맵핑 지원
  - 이 기능은 권장하지 않습니다. (스프링 부트에서는 기본으로 이 기능을 사용하지 않도록 설정 해 줌)
    - 보안 이슈 (RFD Attack)
    - URI 변수, Path 매개변수, URI 인코딩을 사용할 때 할 때 불명확 함.

- RFD Attack
  - https://www.trustwave.com/en-us/resources/blogs/spiderlabs-blog/reflected-file-download-a-new-web-attack-vector/
  - https://www.owasp.org/index.php/Reflected_File_Download
  - https://pivotal.io/security/cve-2015-5211

# HTTP 요청 맵핑하기 미디어 타입 맵핑

- 특정한 타입의 데이터를 담고 있는 요청만 처리하는 핸들러
  - @RequestMapping(consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
  - Content-Type 헤더로 필터링
  - 매치 되는 않는 경우에 415 Unsupported Media Type 응답

- 특정한 타입의 응답을 만드는 핸들러
  - @RequestMapping(produces=”application/json”)
  - Accept 헤더로 필터링 (하지만 살짝... 오묘함)
  - 매치 되지 않는 경우에 406 Not Acceptable 응답

문자열을 입력하는 대신 MediaType을 사용하면 상수를 (IDE에서) 자동 완성으로 사용할 수 있다.

클래스에 선언한 @RequestMapping에 사용한 것과 조합이 되지 않고 메소드에 사용한
@RequestMapping의 설정으로 덮어쓴다.

Not (!)을 사용해서 특정 미디어 타입이 아닌 경우로 맵핑 할 수도 있다.

# 요청 맵핑하기 헤더와 매개변수

- 특정한 헤더가 있는 요청을 처리하고 싶은 경우
  - @RequestMapping(headers = “key”)
- 특정한 헤더가 없는 요청을 처리하고 싶은 경우
  - @RequestMapping(headers = “!key”)
- 특정한 헤더 키/값이 있는 요청을 처리하고 싶은 경우
  - @RequestMapping(headers = “key=value”)
- 특정한 요청 매개변수 키를 가지고 있는 요청을 처리하고 싶은 경우
  - @RequestMapping(params = “a”)
- 특정한 요청 매개변수가 없는 요청을 처리하고 싶은 경우
  - @RequestMapping(params = “!a”)
- 특정한 요청 매개변수 키/값을 가지고 있는 요청을 처리하고 싶은 경우
  - @RequestMapping(params = “a=b”)

~~~
@GetMapping(value = "/header", headers = HttpHeaders.FROM + "=" + "111")
@RequestMapping
public String helloHeader() {
    return "helloheader";
}

@Test
public void helloHeader() throws Exception {
    mockMvc.perform(get("/hello/jjunpro")
            .header(HttpHeaders.FROM, "111"))
            .andExpect(status().isOk())
            .andDo(print());
}
~~~

# HTTP 요청 맵핑하기 HEAD와 OPTIONS 요청 처리

- 우리가 구현하지 않아도 스프링 웹 MVC에서 자동으로 처리하는 HTTP Method
  - HEAD
  - OPTIONS

- HEAD
  - GET 요청과 동일하지만 응답 본문을 받아오지 않고 응답 헤더만 받아온다.
  - 리소스에 대한 간략한 정보만 확인만 하기 때문에 본문은 보내면 안됩니다.

- OPTIONS
  - 사용할 수 있는 HTTP Method 제공
  - 서버 또는 특정 리소스가 제공하는 기능을 확인할 수 있다.
  - 서버는 Allow 응답 헤더에 사용할 수 있는 HTTP Method 목록을 제공해야 한다.

- 참고
  - https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html
  - https://github.com/spring-projects/spring-framework/blob/master/spring-test/src/test/java/org/springframework/test/web/servlet/samples/standalone/resultmatchers/HeaderAssertionTests.java

~~~
@Test
public void helloOption() throws Exception {
    mockMvc.perform(options("/hello"))
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.FROM))
            .andDo(print());
}
~~~

# 요청 맵핑하기 커스텀 애노테이션

- @RequestMapping 애노테이션을 메타 애노테이션으로 사용하기
  - @GetMapping 같은 커스텀한 애노테이션을 만들 수 있다.
- 메타(Meta) 애노테이션
  - 애노테이션에 사용할 수 있는 애노테이션
  - 스프링이 제공하는 대부분의 애노테이션은 메타 애노테이션으로 사용할 수 있다.
- 조합(Composed) 애노테이션
  - 한개 혹은 여러 메타 애노테이션을 조합해서 만든 애노테이션
  - 코드를 간결하게 줄일 수 있다.
  - 보다 구체적인 의미를 부여할 수 있다.
- @Retention
  - 해당 애노테이션 정보를 언제까지 유지할 것인가.
  - Source: 소스 코드까지만 유지. 즉, 컴파일 하면 해당 애노테이션 정보는 사라진다는 이야기.
  - Class: 컴파인 한 .class 파일에도 유지. 즉 런타임 시, 클래스를 메모리로 읽어오면 해당 정보는 사라진다.
  - Runtime: 클래스를 메모리에 읽어왔을 때까지 유지! 코드에서 이 정보를 바탕으로 특정 로직을 실행할 수 있다.
- @Target
  - 해당 애노테이션을 어디에 사용할 수 있는지 결정한다.
- @Documented
  - 해당 애노테이션을 사용한 코드의 문서에 그 애노테이션에 대한 정보를 표기할지 결정한다.
- 메타 애노테이션
  - https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-meta-annotations
  - https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/annotation/AliasFor.html

# 핸들러 메소드 아규먼트와 리턴 타입

핸들러 메소드 아규먼트: 주로 요청 그 자체 또는 요청에 들어있는 정보를 받아오는데 사용한다.

- 핸들러 메소드 아규먼트 설명

- 요청 또는 응답 자체에 접근 가능한 API
  - WebRequest (low level)
  - NativeWebRequest
  - ServletRequest(Response)
  - HttpServletRequest(Response)

- 요청 본문을 읽어오거나, 응답 본문을 쓸 때 사용할 수 있는 API
  - InputStream
  - Reader
  - OutputStream
  - Writer

- 스프링 5, HTTP/2 리소스 푸쉬에 사용
  - PushBuilder 

- GET, POST, ... 등에 대한 정보
  - HttpMethod 

- LocaleResolver가 분석한 요청의 Locale 정보
  - Locale
  - TimeZone
  - ZoneId

- URI 템플릿 변수 읽을 때 사용
  - @PathVariable 

- URI 경로 중에 키/값 쌍을 읽어 올 때 사용
  - @MatrixVariable 

- @RequestParam
  - 서블릿 요청 매개변수 값을 선언한 메소드 아규먼트 타입으로 변환해준다.
  - 단순 타입인 경우에 이 애노테이션을 생략할 수 있다.

- 요청 헤더 값을 선언한 메소드 아규먼트 타입으로 변환해준다.
  - @RequestHeader 

- 참고 URL
  - https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-arguments

- 핸들러 메소드 리턴: 주로 응답 또는 모델을 랜더링할 뷰에 대한 정보를 제공하는데 사용한다.

- @ResponseBody 
  - 리턴 값을 HttpMessageConverter를 사용해 응답 본문으로사용한다.

- 응답 본문 뿐 아니라 헤더 정보까지, 전체 응답을 만들 때 사용한다.
  - 응답 state 코드 상태, 응답해더, 응답 본문 등을 셋팅할 수 있습니다.
  - Rest API 사용시 유용한 코드 
  - HttpEntity
  - ReponseEntity

- String 
  - `ViewResolver를 사용`해서 뷰를 찾을 때 사용할 뷰 이름.

- View 
  - 암묵적인 모델 정보를 랜더링할 뷰 인스턴스

- @GetMapping("url name") 요청에서 View 이름을 찾습니다.
- (RequestToViewNameTranslator를 통해서) 암묵적으로 판단한 뷰 랜더링할 때 사용할 모델 정보
  - Map
  - Model

- @ModelAttribute
  - (RequestToViewNameTranslator를 통해서) 암묵적으로 판단한 뷰 랜더링할 때 사용할 모델 정보에 추가한다.
  - 이 애노테이션은 생략할 수 있다.

- 참고 URL
  - https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-return-types

# 핸들러 메소드 URI 패턴

- @PathVariable
  - 요청 URI 패턴의 일부를 핸들러 메소드 아규먼트로 받는 방법.
  - 타입 변환 지원.
  - (기본)값이 반드시 있어야 한다.
  - Optional 지원.
- @MatrixVariable
  - 요청 URI 패턴에서 키/값 쌍의 데이터를 메소드 아규먼트로 받는 방법
  - 타입 변환 지원.
  - (기본)값이 반드시 있어야 한다.
  - Optional 지원.
  - 이 기능은 기본적으로 비활성화 되어 있음. 활성화 하려면 다음과 같이 설정해야 함.

~~~
@Configuration
  public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
      UrlPathHelper urlPathHelper = new UrlPathHelper();
      urlPathHelper.setRemoveSemicolonContent(false);
    configurer.setUrlPathHelper(urlPathHelper);
  }
}
~~~

- 참고
  - https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-typeconversion
  - https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-matrix-variables

# 핸들러 메소드 @RequestMapping

- @RequestParam
  - 요청 매개변수에 들어있는 `단순 타입 데이터를 메소드 아규먼트로 받아올 수 있다.`
  - 값이 반드시 있어야 한다.
    - required=false 또는 Optional을 사용해서 부가적인 값으로 설정할 수도 있다.

~~~
@GetMapping("/hello/{id}?name=jjunpro")
public Event getEvent(@RequestParam(name = "name",required = false, defaultValue = "hello") String nameObject) { ... }
~~~

- String이 아닌 값들은 타입 컨버전을 지원한다.
- Map<String, String> 또는 MultiValueMap<String, String>에 사용해서 모든 요청 매개변수를 받아 올 수도 있다.
- 이 애노테이션은 생략 할 수 잇다.

- 요청 매개변수란?
  - 쿼리 매개변수
  - 폼 데이터

- 참고
  - https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-requestparam

# 폼 서브밋 (타임리프)

- ${...} : Variable expressions.
- *{...} : Selection expressions.
- #{...} : Message (i18n) expressions.
- @{...} : Link (URL) expressions.
- ~{...} : Fragment expressions.

- 참고
  - https://www.thymeleaf.org/doc/articles/standarddialect5minutes.html
  - https://www.getpostman.com/downloads/

> form.html

~~~
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
  <form action="#" th:action="@{/templates/events}" method="post" th:object="${event}">
    <input type="text" title="name" th:field="*{name}">
    <input type="text" title="limit" th:field="*{limit}">
    <button type="submit">Create</button>
  </form>
</body>
</html>
~~~

> Event.class

~~~
public class Event {
    private String name;
    private Integer limit;
}
~~~

> EventController.class

~~~
@Controller
public class EventController {

    @PostMapping("/events")
    @ResponseBody
    public Event getEvent(
            @RequestParam String name,
            @RequestParam Integer limit
    ) {
        Event event = new Event();
        event.setName(name);
        event.setLimit(limit);

        return event;
    }

    @GetMapping("/events/form")
    public String eventsForm(Model model) {
        Event event = new Event();
        event.setLimit(50);
        model.addAttribute("event", event);

        return "events/form";
    }
}
~~~

> Test.class

~~~
public void event() throws Exception {
    mockMvc.perform(get("/events/form"))
            .andExpect(view().name("/events/form"))
            .andExpect(model().attributeExists("event"))
            .andDo(print());
}
~~~