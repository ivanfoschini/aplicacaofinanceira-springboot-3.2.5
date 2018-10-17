package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaJuridica;
import java.util.List;
import org.springframework.validation.BindingResult;

public interface ClientePessoaJuridicaService {

    void delete(long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<ClientePessoaJuridica> findAll();

    ClientePessoaJuridica findById(long id) throws NotFoundException;    
    
    ClientePessoaJuridica save(ClientePessoaJuridica clientePessoaJuridica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException;        
    
    ClientePessoaJuridica update(long id, ClientePessoaJuridica clientePessoaJuridica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException;
}
