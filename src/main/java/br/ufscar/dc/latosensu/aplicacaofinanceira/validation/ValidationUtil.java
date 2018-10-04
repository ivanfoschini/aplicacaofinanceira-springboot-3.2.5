package br.ufscar.dc.latosensu.aplicacaofinanceira.validation;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cliente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Endereco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.CidadeRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationUtil {
    
    public void validate(BindingResult bindingResult) throws ValidationException {
        List<String> errors = new ArrayList();
        
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();            

            for (FieldError error: fieldErrors) {
                errors.add(error.getDefaultMessage());
            }

            throw new ValidationException(errors);
        }
    }
    
    public void validate(List<FieldError> fieldErrors) throws ValidationException {
        List<String> errors = new ArrayList();
        
        for (FieldError error: fieldErrors) {
            errors.add(error.getDefaultMessage());
        }
        
        Collections.sort(errors);
        
        throw new ValidationException(errors);
    }

    public void validateAgenciaAndEndereco(Agencia agencia, BindingResult bindingResult) throws ValidationException {
        List<FieldError> enderecoFieldErrors = validateEndereco(agencia.getEndereco());
        
        if (bindingResult.hasErrors() || !enderecoFieldErrors.isEmpty()) {
            List<FieldError> fieldErrors = new ArrayList<>(); 
            
            fieldErrors.addAll(bindingResult.getFieldErrors());
            fieldErrors.addAll(enderecoFieldErrors);
            
            new ValidationUtil().validate(fieldErrors);
        }
    }

    public void validateCidades(Cliente cliente, CidadeRepository cidadeRepository, MessageSource messageSource) throws NotFoundException, ValidationException {
        for (Endereco endereco: cliente.getEnderecos()) {            
            List<FieldError> enderecoFieldErrors = validateEndereco(endereco);

            if (!enderecoFieldErrors.isEmpty()) {
                List<FieldError> fieldErrors = new ArrayList<>(); 

                fieldErrors.addAll(enderecoFieldErrors);

                new ValidationUtil().validate(fieldErrors);
            }
            
            if (endereco.getCidade() != null) {            
                Cidade cidade = cidadeRepository.findById(endereco.getCidade().getId().longValue());

                if (cidade == null) {
                    throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
                }        
            } else {
                throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
            }
        }
    }    
    
    private List<FieldError> validateEndereco(Endereco endereco) {
        Set<ConstraintViolation<Endereco>> enderecoViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(endereco);
        
        List<FieldError> fieldErrors = new ArrayList<>();  
        
        for (ConstraintViolation<Endereco> violation: enderecoViolations) {
            FieldError enderecoFieldError = new FieldError("endereco", "field", violation.getMessage());
            
            fieldErrors.add(enderecoFieldError);
        }
        
        return fieldErrors;
    }
}
