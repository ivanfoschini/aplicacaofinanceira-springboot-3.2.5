package br.ufscar.dc.latosensu.aplicacaofinanceira.exception.handler;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ForbiddenException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.MoreThanOneAccountClientException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.MoreThanOneAccountOwnershipException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NoAccountOwnershipException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.UnauthorizedException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.BAD_REQUEST, exception, "Validation errors", errors);

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(EmptyCollectionException.class)
    protected ResponseEntity<Object> handleEmptyCollectionException(EmptyCollectionException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.UNPROCESSABLE_ENTITY, exception, "Object association is empty", exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<Object> handleForbiddenException(ForbiddenException exception) {
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MoreThanOneAccountClientException.class)
    protected ResponseEntity<Object> handleMoreThanOneAccountClientException(MoreThanOneAccountClientException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.UNPROCESSABLE_ENTITY, exception, "More than one account client was supplied", exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(MoreThanOneAccountOwnershipException.class)
    protected ResponseEntity<Object> handleMoreThanOneAccountOwnershipException(MoreThanOneAccountOwnershipException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.UNPROCESSABLE_ENTITY, exception, "More than one account ownership was supplied", exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(NoAccountOwnershipException.class)
    protected ResponseEntity<Object> handleNoAccountOwnershipException(NoAccountOwnershipException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.UNPROCESSABLE_ENTITY, exception, "No account ownership was supplied", exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(NotEmptyCollectionException.class)
    protected ResponseEntity<Object> handleNotEmptyCollectionException(NotEmptyCollectionException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.UNPROCESSABLE_ENTITY, exception, "Object association is not empty", exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(NotFoundException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.NOT_FOUND, exception, "Object not found", exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(NotUniqueException.class)
    protected ResponseEntity<Object> handleNotUnique(NotUniqueException exception) {
        CustomExceptionError customExceptionError = new CustomExceptionError(HttpStatus.UNPROCESSABLE_ENTITY, exception, "Unique database constraint violated", exception.getMessage());

        return new ResponseEntity<>(customExceptionError, new HttpHeaders(), customExceptionError.getStatus());
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException exception) {
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
