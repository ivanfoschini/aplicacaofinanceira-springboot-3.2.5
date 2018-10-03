package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import java.util.List;
import org.springframework.validation.BindingResult;

public interface AgenciaService {
    
    void delete(long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<Agencia> findAll();

    Agencia findById(long id) throws NotFoundException;    
    
    Agencia save(Agencia agencia, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException;        
    
    Agencia update(long id, Agencia agencia, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException;    
}
