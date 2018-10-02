package br.ufscar.dc.latosensu.aplicacaofinanceira.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {

    List<String> errors = new ArrayList<>();
    
    public List<String> getErrors() {
        return this.errors;
    }
    
    public ValidationException(List<String> errors) {
        this.errors = errors;
    }    
}
