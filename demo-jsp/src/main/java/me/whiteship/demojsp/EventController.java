package me.whiteship.demojsp;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
