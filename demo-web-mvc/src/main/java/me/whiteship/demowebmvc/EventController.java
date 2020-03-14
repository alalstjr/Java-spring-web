package me.whiteship.demowebmvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
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

    /* 멀티 폼 서브밋 */
    @GetMapping("/events/list")
    public String getEvents(Model model) {
        Event event = new Event();
        event.setName("spring");
        event.setLimit(10);

        ArrayList<Event> events = new ArrayList<>();
        events.add(event);

        model.addAttribute(events);

        return "/events/list";
    }

    @GetMapping("/events/form/name")
    public String eventFormName(Model model) {
        /* 어노테이션 @SessionAttributes("event") 선언으로 인해서 Session 에 event 객체가 저장됩니다. */
        model.addAttribute("event", new Event());

        return "events/form-name";
    }

    @PostMapping("/events/form/name")
    public String eventFormNameSubmit(
            @Valid @ModelAttribute Event event,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "events/form-name";
        }

        return "redirect:/events/form/limit";
    }

    /* @ModelAttribute 어노테이션으로 세션에 저장된 Event 객체를 가져옵니다. */
    @GetMapping("/events/form/limit")
    public String eventFormLimit(@ModelAttribute Event event, Model model) {
        model.addAttribute("event", event);

        return "events/form-limit";
    }

    @PostMapping("/events/form/limit")
    public String eventFormLimitSubmit(
            @Valid @ModelAttribute Event event,
            BindingResult bindingResult,
            SessionStatus sessionStatus,
            @SessionAttribute LocalDateTime visitTime
    ) {
        if(bindingResult.hasErrors()) {
            return "events/form-limit";
        }

        sessionStatus.isComplete();

        return "redirect:/events/list";
    }

}
