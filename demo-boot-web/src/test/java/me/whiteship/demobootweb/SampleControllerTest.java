package me.whiteship.demobootweb;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.StringWriter;
import javax.xml.transform.stream.StreamResult;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.oxm.Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
public class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    /* Web Config 에서 등록한 Jaxb2Marshaller Bean 을 받아옵니다. */
    @Autowired
    Marshaller marshaller;

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

    @Test
    public void stringMessage() throws Exception {
        mockMvc.perform(get("/message").content("hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"))
                .andDo(print());
    }

    /*
    contentType : 사용자가 본문에 보내는 정보가 어떠한 타입인지 서버에 알려주는 정보
    accept : 요청의 대한 응답으로 어떠한 타입을 원하는지 알려주는 정보
    */
    @Test
    public void jsonMessage() throws Exception {
        Person person = new Person();
        person.setId(2020L);
        person.setName("jjunpro");

        String personJson = objectMapper.writeValueAsString(person);

        mockMvc.perform(get("/message")
                .content(personJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"))
                .andExpect(jsonPath("$.id").value(2020))
                .andDo(print());
    }

    @Test
    public void xmlMessage() throws Exception {
        Person person = new Person();
        person.setId(2020L);
        person.setName("jjunpro");

        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        marshaller.marshal(person, streamResult);
        String xmlString = stringWriter.toString();

        mockMvc.perform(get("/jsonMessage")
                .content(xmlString)
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(xpath("person/id").string("2020"))
                .andDo(print());
    }
}