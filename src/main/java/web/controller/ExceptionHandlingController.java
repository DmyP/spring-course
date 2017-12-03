package web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@ControllerAdvice
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ExceptionHandlingController {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        String message = Optional.ofNullable(ex.getMessage()).orElse("Unknown error message");
        model.addAttribute("message", message);
        return "error";
    }
}
