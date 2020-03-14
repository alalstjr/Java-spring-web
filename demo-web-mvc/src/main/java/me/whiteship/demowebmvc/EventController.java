package me.whiteship.demowebmvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
