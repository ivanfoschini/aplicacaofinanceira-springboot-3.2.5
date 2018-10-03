package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Estado;
import java.util.List;
import org.springframework.validation.BindingResult;

public interface EstadoService {
    
    void delete(long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<Estado> findAll();

    Estado findById(long id) throws NotFoundException;    
    
    Estado save(Estado estado, BindingResult bindingResult) throws NotUniqueException, ValidationException;        
    
    Estado update(long id, Estado estado, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException;
}
