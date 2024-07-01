package com.vpolosov.trainee.merge_xml.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Тестирование контроллера MergeController")
public class MergeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private static Path getFixturesPath() {
        return Paths.get("src", "test", "java",
                "com", "vpolosov", "trainee", "merge_xml", "test_fixtures", "Ok")
            .toAbsolutePath().normalize();
    }

    private static Path getPathForTotalFIle() {
        return Paths.get("src", "test", "java",
                "com", "vpolosov", "trainee", "merge_xml", "test_fixtures", "Ok", "Total.xml")
            .toAbsolutePath().normalize();
    }

    @DisplayName("Тест контроллера patchXml() когда передан корректный путь к файлам и запрос выполняется впервые")
    @Test
    void patchXml_whenValidPathWithFirstRequest_thenReturnSuccessString() throws Exception {
        if (Files.exists(getPathForTotalFIle())) {
            Files.delete(getPathForTotalFIle());
        }

        String validPath = getFixturesPath().toString();

        MockHttpServletResponse responsePost = mockMvc
            .perform(
                post("/xml")
                    .contentType(MediaType.TEXT_PLAIN)
                    .content(validPath)
            )
            .andReturn()
            .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);
        assertThat(responsePost.getContentAsString()).isEqualTo("Total.xml was created!");
    }
}
