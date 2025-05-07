package dev.fwegrzyn.charity_fund_manager.controller;

import dev.fwegrzyn.charity_fund_manager.model.CollectionBox;
import dev.fwegrzyn.charity_fund_manager.repository.CollectionBoxRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boxes")
public class CollectionBoxController {
    private final CollectionBoxRepository collectionBoxRepository;

    public CollectionBoxController(CollectionBoxRepository collectionBoxRepository) {
        this.collectionBoxRepository = collectionBoxRepository;
    }

    @GetMapping("/")
    public Iterable<CollectionBox> findAll() {
        return collectionBoxRepository.findAll();
    }

}
