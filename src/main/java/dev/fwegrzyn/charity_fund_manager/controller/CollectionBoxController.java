package dev.fwegrzyn.charity_fund_manager.controller;

import dev.fwegrzyn.charity_fund_manager.dto.request.DonateRequest;
import dev.fwegrzyn.charity_fund_manager.dto.response.CollectionBoxDTO;
import dev.fwegrzyn.charity_fund_manager.model.CollectionBox;
import dev.fwegrzyn.charity_fund_manager.service.CollectionBoxService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boxes")
public class CollectionBoxController {
    private final CollectionBoxService collectionBoxService;

    public CollectionBoxController(CollectionBoxService collectionBoxService) {
        this.collectionBoxService = collectionBoxService;
    }

    @PostMapping
    public ResponseEntity<CollectionBox> createBox() {
        CollectionBox createdBox = collectionBoxService.createBox();
        return new ResponseEntity<>(createdBox, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CollectionBoxDTO>> getBoxes() {
        return new ResponseEntity<>(collectionBoxService.getBoxes(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBox(@PathVariable("id") Integer id) {
        collectionBoxService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/assign")
    public ResponseEntity<Void> assignBoxToEvent(@RequestParam("box_id") Integer boxId, @RequestParam("event_id") Integer eventId) {
        collectionBoxService.assignBoxToEvent(boxId, eventId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/donate")
    public ResponseEntity<Void> donateMoneyToBox(@RequestBody @Valid DonateRequest request) {
        collectionBoxService.donateMoneyToBox(request.boxId(), request.currency(), request.amount());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/transfer")
    public ResponseEntity<Void> transferMoneyToEvent(@RequestParam("box_id") Integer boxId) {
        collectionBoxService.transferMoneyToEvent(boxId);
        return ResponseEntity.noContent().build();
    }
}
