package com.gallery.app.problem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class LocalizableThrowableProblemHandler {

    @ExceptionHandler
    public ResponseEntity<LocalizableProblem> handleLocalizableProblem(LocalizableException exception) {
        LocalizableProblem problem = new LocalizableProblem(exception);
        return ResponseEntity.status(problem.getStatus()).body(problem);
    }
}
