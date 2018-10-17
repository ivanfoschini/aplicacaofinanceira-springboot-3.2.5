package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaCorrente;
import java.util.List;
import org.springframework.validation.BindingResult;

public interface ContaCorrenteService {
    
    void delete(long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<ContaCorrente> findAll();

    ContaCorrente findById(long id) throws NotFoundException;    
    
    ContaCorrente save(ContaCorrente contaCorrente, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException;        
    
    ContaCorrente update(long id, ContaCorrente contaCorrente, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException;    
}
