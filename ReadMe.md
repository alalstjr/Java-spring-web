--------------------
# Java Spring Web
--------------------

# 목차

- [1. 스프링 MVC](#스프링-MVC)

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