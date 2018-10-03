package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import java.util.List;
import org.springframework.validation.BindingResult;

public interface BancoService {
    
    void delete(long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<Banco> findAll();

    Banco findById(long id) throws NotFoundException;    
    
    Banco save(Banco banco, BindingResult bindingResult) throws NotUniqueException, ValidationException;        
    
    Banco update(long id, Banco banco, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException;
}
