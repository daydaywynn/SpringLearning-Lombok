package guru.springframework.spring6restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice //Makes this get picked up globally for all controller
public class ExceptionController {

    /**
     * If something in this controller throws a not found exception
     * it'll be handled by NotFoundException class
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(){
        return ResponseEntity.notFound().build();
    }
}
