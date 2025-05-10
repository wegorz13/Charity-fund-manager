package dev.fwegrzyn.charity_fund_manager.controller;

import dev.fwegrzyn.charity_fund_manager.dto.response.CollectionBoxDTO;
import dev.fwegrzyn.charity_fund_manager.model.CollectionBox;
import dev.fwegrzyn.charity_fund_manager.service.CollectionBoxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
        boolean deleted = collectionBoxService.deleteById(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/assign")
    public ResponseEntity<Void> assignBoxToEvent(@RequestParam Integer boxId, @RequestParam Integer eventId) {
        collectionBoxService.assignBoxToEvent(boxId, eventId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/donate")
    public ResponseEntity<Void> donateMoneyToBox(@RequestBody Integer boxId, @RequestBody String currency, @RequestBody BigDecimal amount) {
        collectionBoxService.donateMoneyToBox(boxId, currency, amount);
        return ResponseEntity.noContent().build();
    }
}
