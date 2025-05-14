package dev.fwegrzyn.charity_fund_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fwegrzyn.charity_fund_manager.dto.request.CreateFundraisingEventRequest;
import dev.fwegrzyn.charity_fund_manager.dto.response.FundraisingEventDTO;
import dev.fwegrzyn.charity_fund_manager.model.FundraisingEvent;
import dev.fwegrzyn.charity_fund_manager.service.FundraisingEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FundraisingEventController.class)
@ContextConfiguration(classes = FundraisingEventControllerTest.TestConfig.class)
public class FundraisingEventControllerTest {

    @Configuration
    @Import(FundraisingEventController.class)
    static class TestConfig {
        @Bean
        public FundraisingEventService fundraisingEventService() {
            return mock(FundraisingEventService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FundraisingEventService fundraisingEventService;

    @Test
    void createEvent_WithValidRequest_ShouldReturnCreatedEvent() throws Exception {
        // Given
        CreateFundraisingEventRequest request = new CreateFundraisingEventRequest("Siepomaga", "USD");
        FundraisingEvent createdEvent = new FundraisingEvent(1, "Siepomaga", 1, BigDecimal.ZERO);

        when(fundraisingEventService.createEvent("Siepomaga", "USD")).thenReturn(createdEvent);

        // When & Then
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Siepomaga")))
                .andExpect(jsonPath("$.currencyId", is(1)))
                .andExpect(jsonPath("$.money", is(0)));
    }

    @Test
    void createEvent_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        CreateFundraisingEventRequest request = new CreateFundraisingEventRequest("", "USD");

        // When & Then
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getEventReports_ShouldReturnListOfEvents() throws Exception {
        // Given
        List<FundraisingEventDTO> events = Arrays.asList(
                new FundraisingEventDTO(1, "Siepomaga", BigDecimal.valueOf(1000), "USD"),
                new FundraisingEventDTO(2, "Caritas", BigDecimal.valueOf(2500), "EUR")
        );

        when(fundraisingEventService.getEventReports()).thenReturn(events);

        // When & Then
        mockMvc.perform(get("/api/events/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Siepomaga")))
                .andExpect(jsonPath("$[0].money", is(1000)))
                .andExpect(jsonPath("$[0].currency", is("USD")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Caritas")))
                .andExpect(jsonPath("$[1].money", is(2500)))
                .andExpect(jsonPath("$[1].currency", is("EUR")));
    }
}