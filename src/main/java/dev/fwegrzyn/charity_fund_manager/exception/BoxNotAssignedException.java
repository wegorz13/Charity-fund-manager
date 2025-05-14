package dev.fwegrzyn.charity_fund_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BoxNotAssignedException extends RuntimeException {
    public BoxNotAssignedException(Integer boxId) {
        super("Box " + boxId + " is not assigned to an event");
    }
}
