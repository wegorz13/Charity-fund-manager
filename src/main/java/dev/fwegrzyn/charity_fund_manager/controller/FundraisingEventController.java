package dev.fwegrzyn.charity_fund_manager.controller;

import dev.fwegrzyn.charity_fund_manager.dto.request.CreateFundraisingEventRequest;
import dev.fwegrzyn.charity_fund_manager.model.FundraisingEvent;
import dev.fwegrzyn.charity_fund_manager.service.FundraisingEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/events")
public class FundraisingEventController {
    private final FundraisingEventService fundraisingEventService;

    public FundraisingEventController(FundraisingEventService fundraisingEventService) {
        this.fundraisingEventService = fundraisingEventService;
    }

    @PostMapping
    public ResponseEntity<FundraisingEvent> createEvent(@RequestBody CreateFundraisingEventRequest request) {
        FundraisingEvent createdEvent = fundraisingEventService.createEvent(request.name(), request.currency());
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }
}
