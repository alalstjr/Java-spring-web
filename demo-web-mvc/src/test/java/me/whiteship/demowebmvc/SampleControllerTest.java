package me.whiteship.demowebmvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
public class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() throws Exception {
        mockMvc.perform(get("/hello/jjunpro"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"))
                .andExpect(handler().handlerType(SampleController.class))
                .andExpect(handler().methodName("hello"))
                .andDo(print());
    }

    @Test
    public void helloHeader() throws Exception {
        mockMvc.perform(get("/hello/jjunpro")
                .header(HttpHeaders.FROM, "111"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void helloOption() throws Exception {
        mockMvc.perform(options("/hello"))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.FROM))
                .andDo(print());
    }

    @Test
    public void helloCustom() throws Exception {
        mockMvc.perform(get("/helloCustom"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}