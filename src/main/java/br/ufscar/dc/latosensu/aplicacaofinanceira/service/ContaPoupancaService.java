package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaPoupanca;
import java.util.List;
import org.springframework.validation.BindingResult;

public interface ContaPoupancaService {
    
    void delete(long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<ContaPoupanca> findAll();

    ContaPoupanca findById(long id) throws NotFoundException;    
    
    ContaPoupanca save(ContaPoupanca contaPoupanca, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException;        
    
    ContaPoupanca update(long id, ContaPoupanca contaPoupanca, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException;    
}
