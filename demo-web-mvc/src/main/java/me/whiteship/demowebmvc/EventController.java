package me.whiteship.demowebmvc;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("event")
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

    @GetMapping("/events/form/{name}")
    public String eventsForm(@Validated(Event.ValidateName.class) Model model) {
        Event event = new Event();
        event.setLimit(50);
        model.addAttribute("event", event);

        return "events/form";
    }

    @GetMapping("/session")
    public String setSession(HttpSession httpSession, SessionStatus sessionStatus) {
        httpSession.setAttribute("event", "setEvent");
        sessionStatus.isComplete();
        return "events/form";
    }
}
