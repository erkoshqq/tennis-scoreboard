package org.mserz_o.springcourse.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleMatchNotFound(NotFoundException exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage());

        return "error";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException exception, Model model) {

        String message = switch (exception.getName()) {
            case "uuid" ->
                    "Некорректный идентификатор матча";

            case "winnerSide" ->
                    "Некорректная сторона игрока";

            default ->
                    "Некорректный параметр: " + exception.getName();
        };

        model.addAttribute("errorMessage", message);

        return "error";
    }
}
