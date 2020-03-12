package me.whiteship.demobootweb;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        mockMvc
                .perform(
                        get("/hello").param("id", "1")
                )
                .andExpect(content().string("hello"))
                .andDo(print());
    }

    @Test
    public void helloStatic() throws Exception {
        mockMvc.perform(get("/mobile/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("mobile")))
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
                .andDo(print());
    }
}