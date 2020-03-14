package me.whiteship.demowebmvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(method = RequestMethod.GET, value = "/helloCustom")
@Retention(RetentionPolicy.RUNTIME)
public @interface GetHelloMapping {

}
