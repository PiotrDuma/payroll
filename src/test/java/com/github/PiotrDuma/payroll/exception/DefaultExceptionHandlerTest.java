package com.github.PiotrDuma.payroll.exception;

import com.github.PiotrDuma.payroll.PayrollApplication;
import com.github.PiotrDuma.payroll.tools.JsonFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExceptionControllerMock.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {DefaultExceptionHandlerTest.Config.class})
class DefaultExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldHandleRuntimeException() throws Exception{
        String exceptionMessage = "Something went wrong";
        String expected = """
        {
            "requestURL": "/test/runtime",
            "errorMessage": "Something went wrong",
            "statusCode": 500,
            "timestamp": "2024-01-02T03:40:50.906435344"
        }
        """;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/test/runtime"))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> assertEquals("application/json", result.getResponse().getContentType()))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals(exceptionMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();

        assertEquals(JsonFormatter.getRawString(expected), result);
    }

    @Test
    void shouldHandleResourceNotFoundException() throws Exception{
        String exceptionMessage = "Something is not found";
        String expected = """
        {
            "requestURL": "/test/resource",
            "errorMessage": "Something is not found",
            "statusCode": 404,
            "timestamp": "2024-01-02T03:40:50.906435344"
        }
        """;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/test/resource"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("application/json", result.getResponse().getContentType()))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals(exceptionMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();

        assertEquals(JsonFormatter.getRawString(expected), result);
    }

    @Test
    void shouldHandlePathNotFoundException() throws Exception{
        String exceptionMessage = "No endpoint GET localhost:8080/path.";
        String expected = """
        {
            "requestURL": "/test/path",
            "errorMessage": "The requested URL does not exist",
            "statusCode": 404,
            "timestamp": "2024-01-02T03:40:50.906435344"
        }
        """;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/test/path"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoHandlerFoundException))
                .andExpect(result -> assertEquals(exceptionMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(JsonFormatter.getRawString(expected), result);
    }


    @TestConfiguration
    @Import(PayrollApplication.class)
    static class Config{
        private static final ZonedDateTime CURRENT_TIME = ZonedDateTime.of(
                2024,
                1,
                2,
                3,
                40,
                50,
                906435344,
                ZoneId.of("UTC"));

        @Bean
        @Primary
        public Clock getClock(){
            return Clock.fixed(CURRENT_TIME.toInstant(), CURRENT_TIME.getZone());
        }
    }
}