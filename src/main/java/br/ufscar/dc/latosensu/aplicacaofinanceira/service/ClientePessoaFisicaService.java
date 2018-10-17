package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaFisica;
import java.util.List;
import org.springframework.validation.BindingResult;

public interface ClientePessoaFisicaService {

    void delete(long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<ClientePessoaFisica> findAll();

    ClientePessoaFisica findById(long id) throws NotFoundException;    
    
    ClientePessoaFisica save(ClientePessoaFisica clientePessoaFisica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException;        
    
    ClientePessoaFisica update(long id, ClientePessoaFisica clientePessoaFisica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException;
}
