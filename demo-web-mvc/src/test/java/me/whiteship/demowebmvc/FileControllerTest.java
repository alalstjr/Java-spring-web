package me.whiteship.demowebmvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void uploadFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "text.txt",
                "text/plain",
                "hello file".getBytes()
        );

        mockMvc.perform(multipart("/file").file(multipartFile))
                .andExpect(status().is3xxRedirection());
    }
}