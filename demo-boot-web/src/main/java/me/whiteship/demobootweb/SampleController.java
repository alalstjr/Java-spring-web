package me.whiteship.demobootweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/hello")
    public String hello(@RequestParam("id") Person person) {
        return "hello" + person.getName();
    }

    /**
     * @RequestBody 어노테이션을 사용하면 본문에 들어있는 메세지를 Http Message 컨버터를 사용해서 컨버전을 합니다.
     * @ResponseBody 어노테이션이 붙어있으면 return 값을 응답의 본문으로 넣어줍니다.
     * 하지만 @RestContoller 어노테이션이 상위에 붙어있기 때문에 @ResponseBody 값은 디폴트값입니다. 생략 가능
     * */
    @GetMapping("/message")
    // @ResponseBody
    public String message(@RequestBody String body) {
        return body;
    }

    /**
     * 입력값도 JSON 반환하는 값도 JSON
     * */
    @GetMapping("/jsonMessage")
    public Person jsonMessage(@RequestBody Person person) {
        return person;
    }
}
