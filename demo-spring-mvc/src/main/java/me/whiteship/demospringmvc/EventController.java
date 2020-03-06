package me.whiteship.demospringmvc;

import me.whiteship.demospringmvc.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
