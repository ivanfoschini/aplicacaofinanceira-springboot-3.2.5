package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cliente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaJuridica;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Endereco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.CidadeRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ClientePessoaJuridicaRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.EnderecoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.validation.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional
public class ClientePessoaJuridicaServiceImpl implements ClientePessoaJuridicaService {

    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private ClientePessoaJuridicaRepository clientePessoaJuridicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        Cliente cliente = clientePessoaJuridicaRepository.findById(id);

        if (cliente == null || !(cliente instanceof ClientePessoaJuridica)) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }
        
        if (!cliente.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("clienteEhCorrentista", null, null));
        } 
        
        for (Endereco endereco: cliente.getEnderecos()) {
            enderecoRepository.delete(endereco);
        }
        
        clientePessoaJuridicaRepository.delete((ClientePessoaJuridica) cliente);
    }

    @Override
    public List<ClientePessoaJuridica> findAll() {
        return clientePessoaJuridicaRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }    

    @Override
    public ClientePessoaJuridica findById(long id) throws NotFoundException {
        Cliente cliente = clientePessoaJuridicaRepository.findById(id);

        if (cliente == null || !(cliente instanceof ClientePessoaJuridica)) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }        
        
        return (ClientePessoaJuridica) cliente;
    }

    @Override
    public ClientePessoaJuridica save(ClientePessoaJuridica clientePessoaJuridica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);
        new ValidationUtil().validateCidades(clientePessoaJuridica, cidadeRepository, messageSource);
        
        if (clientePessoaJuridica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }
        
        ClientePessoaJuridica savedClientePessoaJuridica = clientePessoaJuridicaRepository.save(clientePessoaJuridica);
        
        for (Endereco endereco: clientePessoaJuridica.getEnderecos()) {
            endereco.setCliente(clientePessoaJuridica);
            enderecoRepository.save(endereco);
        }

        return savedClientePessoaJuridica;
    }

    @Override
    public ClientePessoaJuridica update(long id, ClientePessoaJuridica clientePessoaJuridica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);   
        new ValidationUtil().validateCidades(clientePessoaJuridica, cidadeRepository, messageSource);
        
        if (clientePessoaJuridica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }
        
        ClientePessoaJuridica clientePessoaJuridicaToUpdate = findById(id);

        if (clientePessoaJuridicaToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }
        
        clientePessoaJuridicaToUpdate.setNome(clientePessoaJuridica.getNome());
        clientePessoaJuridicaToUpdate.setStatus(clientePessoaJuridica.getStatus());
        clientePessoaJuridicaToUpdate.setCnpj(clientePessoaJuridica.getCnpj());
        
        List<Endereco> enderecosToRemove = new ArrayList<>();
        
        for (Endereco endereco: clientePessoaJuridicaToUpdate.getEnderecos()) {
            enderecosToRemove.add(endereco);
            enderecoRepository.delete(endereco);
        }
        
        for (Endereco endereco: enderecosToRemove) {
            clientePessoaJuridicaToUpdate.getEnderecos().remove(endereco);
        }
        
        for (Endereco endereco: clientePessoaJuridica.getEnderecos()) {
            endereco.setCliente(clientePessoaJuridicaToUpdate);
            clientePessoaJuridicaToUpdate.getEnderecos().add(endereco);
            enderecoRepository.save(endereco);
        }
        
        return clientePessoaJuridicaRepository.save(clientePessoaJuridicaToUpdate);
    }
}
