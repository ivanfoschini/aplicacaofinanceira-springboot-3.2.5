package br.ufscar.dc.latosensu.aplicacaofinanceira.exception.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpStatus;

public class CustomExceptionError {

    private final HttpStatus status;
    private final String exception;
    private final String cause;
    private final List<String> errors;
    private final String dateTime;

    public CustomExceptionError(HttpStatus status, Exception exception, String cause, List<String> errors) {
        super();
        this.status = status;
        this.exception = exception.getClass().getName();
        this.cause = cause;
        this.errors = errors;
        this.dateTime = formatDateTime();
    }

    public CustomExceptionError(HttpStatus status, Exception exception, String cause, String error) {
        super();
        this.status = status;
        this.exception = exception.getClass().getName();
        this.cause = cause;
        this.errors = List.of(error);
        this.dateTime = formatDateTime();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getException() {
        return exception;
    }

    public String getCause() {
        return cause;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getDateTime() {
        return dateTime;
    }

    private String formatDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return formatter.format(new Date());
    }
}
