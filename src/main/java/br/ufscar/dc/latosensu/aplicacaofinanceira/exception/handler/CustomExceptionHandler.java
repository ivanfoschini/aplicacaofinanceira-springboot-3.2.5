package br.ufscar.dc.latosensu.aplicacaofinanceira.exception.handler;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.UNPROCESSABLE_ENTITY, exception, messageSource.getMessage("causeMethodArgumentNotValidException", null, null), errors);

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(NotEmptyCollectionException.class)
    protected ResponseEntity<Object> handleNotEmptyCollectionException(NotEmptyCollectionException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.UNPROCESSABLE_ENTITY, exception, messageSource.getMessage("causeNotEmptyCollectionException", null, null), exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(NotFoundException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.NOT_FOUND, exception, messageSource.getMessage("causeNotFoundException", null, null), exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(NotUniqueException.class)
    protected ResponseEntity<Object> handleNotUnique(NotUniqueException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.UNPROCESSABLE_ENTITY, exception, messageSource.getMessage("causeNotUniqueException", null, null), exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }
}
