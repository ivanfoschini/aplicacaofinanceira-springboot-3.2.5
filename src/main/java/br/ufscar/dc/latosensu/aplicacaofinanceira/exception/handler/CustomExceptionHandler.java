package br.ufscar.dc.latosensu.aplicacaofinanceira.exception.handler;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.BAD_REQUEST, exception, "Validation errors", errors);

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNotFound(NotFoundException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.NOT_FOUND, exception, "Object not found", exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(NotUniqueException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleNotUnique(NotUniqueException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.UNPROCESSABLE_ENTITY, exception, "Unique database constraint violated", exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }
}
