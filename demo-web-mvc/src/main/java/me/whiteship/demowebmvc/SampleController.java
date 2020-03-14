package me.whiteship.demowebmvc;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class SampleController {

    // @GetMapping({"/hello", "/hi"})
    // @GetMapping("/hello/?")
    // @GetMapping("/hello?")
    // @GetMapping("/hello/*")
    // @GetMapping("/hello/**")
    // @GetMapping("/hello/{name:[a-z]+}")
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

    @GetMapping(value = "/header", headers = HttpHeaders.FROM + "=" + "111")
    @RequestMapping
    public String helloHeader() {
        return "helloheader";
    }

    @GetHelloMapping
    @ResponseBody
    public String helloCustom() {
        return "helloCustom";
    }

    @GetMapping("/hello/{id}?name=jjunpro")
    public Event getEvent(@RequestParam(name = "name",required = false, defaultValue = "hello") String nameObject) {

    }
}
