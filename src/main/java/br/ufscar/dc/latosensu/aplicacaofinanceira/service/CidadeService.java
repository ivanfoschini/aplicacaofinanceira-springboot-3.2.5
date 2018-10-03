package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import java.util.List;
import org.springframework.validation.BindingResult;

public interface CidadeService {
    
    void delete(long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<Cidade> findAll();

    Cidade findById(long id) throws NotFoundException;    
    
    Cidade save(Cidade cidade, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException;        
    
    Cidade update(long id, Cidade cidade, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException;
}
