package dev.fwegrzyn.charity_fund_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BoxNotEmptyException extends RuntimeException {
  public BoxNotEmptyException(Integer boxId) {
    super("Box " + boxId + " must be empty before assignment");
  }
}
