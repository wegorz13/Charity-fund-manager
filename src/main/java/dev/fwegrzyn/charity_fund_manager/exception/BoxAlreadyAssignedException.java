package dev.fwegrzyn.charity_fund_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BoxAlreadyAssignedException extends RuntimeException {
    public BoxAlreadyAssignedException(Integer boxId) {
        super("Box " + boxId + " is already assigned to an event");
    }
}

