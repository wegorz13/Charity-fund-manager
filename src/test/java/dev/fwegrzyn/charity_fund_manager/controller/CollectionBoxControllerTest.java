package dev.fwegrzyn.charity_fund_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fwegrzyn.charity_fund_manager.dto.request.DonateRequest;
import dev.fwegrzyn.charity_fund_manager.dto.response.CollectionBoxDTO;
import dev.fwegrzyn.charity_fund_manager.model.CollectionBox;
import dev.fwegrzyn.charity_fund_manager.model.CollectionBoxBalance;
import dev.fwegrzyn.charity_fund_manager.service.CollectionBoxService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CollectionBoxController.class)
@ContextConfiguration(classes = CollectionBoxControllerTest.TestConfig.class)
public class CollectionBoxControllerTest {

    @Configuration
    @Import(CollectionBoxController.class)
    static class TestConfig {
        @Bean
        public CollectionBoxService collectionBoxService() {
            return mock(CollectionBoxService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CollectionBoxService collectionBoxService;

    @Test
    void createBox_ShouldReturnCreatedBox() throws Exception {
        // Given
        Set<CollectionBoxBalance> balances = new HashSet<>();
        balances.add(new CollectionBoxBalance(1, BigDecimal.ZERO));
        balances.add(new CollectionBoxBalance(2, BigDecimal.ZERO));

        CollectionBox createdBox = new CollectionBox(1, null, balances);

        when(collectionBoxService.createBox()).thenReturn(createdBox);

        // When & Then
        mockMvc.perform(post("/api/boxes"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.eventId").doesNotExist());
    }

    @Test
    void getBoxes_ShouldReturnListOfBoxes() throws Exception {
        // Given
        List<CollectionBoxDTO> boxes = Arrays.asList(
                createBoxDTO(1, true, false),
                createBoxDTO(2, false, true)
        );

        when(collectionBoxService.getBoxes()).thenReturn(boxes);

        // When & Then
        mockMvc.perform(get("/api/boxes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].assigned", is(true)))
                .andExpect(jsonPath("$[0].empty", is(false)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].assigned", is(false)))
                .andExpect(jsonPath("$[1].empty", is(true)));
    }

    @Test
    void deleteBox_ShouldReturnNoContent() throws Exception {
        // Given
        Integer boxId = 1;
        doNothing().when(collectionBoxService).deleteById(boxId);

        // When & Then
        mockMvc.perform(delete("/api/boxes/{id}", boxId))
                .andExpect(status().isNoContent());

        verify(collectionBoxService).deleteById(boxId);
    }

    @Test
    void assignBoxToEvent_ShouldReturnNoContent() throws Exception {
        // Given
        Integer boxId = 1;
        Integer eventId = 2;
        doNothing().when(collectionBoxService).assignBoxToEvent(boxId, eventId);

        // When & Then
        mockMvc.perform(put("/api/boxes/assign")
                        .param("box_id", boxId.toString())
                        .param("event_id", eventId.toString()))
                .andExpect(status().isNoContent());

        verify(collectionBoxService).assignBoxToEvent(boxId, eventId);
    }

    @Test
    void donateMoneyToBox_ShouldReturnNoContent() throws Exception {
        // Given
        DonateRequest request = new DonateRequest(1, "USD", new BigDecimal("100.00"));
        doNothing().when(collectionBoxService).donateMoneyToBox(eq(1), eq("USD"), any(BigDecimal.class));

        // When & Then
        mockMvc.perform(post("/api/boxes/donate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(collectionBoxService).donateMoneyToBox(eq(1), eq("USD"), any(BigDecimal.class));
    }

    @Test
    void transferMoneyToEvent_ShouldReturnNoContent() throws Exception {
        // Given
        Integer boxId = 1;
        doNothing().when(collectionBoxService).transferMoneyToEvent(boxId);

        // When & Then
        mockMvc.perform(post("/api/boxes/transfer/{id}", boxId.toString())).andExpect(status().isNoContent());

        verify(collectionBoxService).transferMoneyToEvent(boxId);
    }

    private CollectionBoxDTO createBoxDTO(Integer id, boolean assigned, boolean empty) {
        CollectionBoxDTO dto = new CollectionBoxDTO();
        dto.setId(id);
        dto.setAssigned(assigned);
        dto.setEmpty(empty);
        return dto;
    }
}