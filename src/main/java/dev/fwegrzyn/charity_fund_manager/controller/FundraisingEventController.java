package dev.fwegrzyn.charity_fund_manager.controller;

import dev.fwegrzyn.charity_fund_manager.model.FundraisingEvent;
import dev.fwegrzyn.charity_fund_manager.repository.FundraisingEventRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/events")
public class FundraisingEventController {
    private final FundraisingEventRepository fundraisingEventRepository;

    public FundraisingEventController(FundraisingEventRepository fundraisingEventRepository) {
        this.fundraisingEventRepository = fundraisingEventRepository;
    }

    @GetMapping("/")
    public Iterable<FundraisingEvent> findAll() {
        return fundraisingEventRepository.findAll();
    }
}
