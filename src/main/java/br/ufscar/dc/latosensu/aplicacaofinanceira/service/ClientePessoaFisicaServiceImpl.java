package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cliente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaFisica;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Endereco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.CidadeRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ClientePessoaFisicaRepository;
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
public class ClientePessoaFisicaServiceImpl implements ClientePessoaFisicaService {

    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private ClientePessoaFisicaRepository clientePessoaFisicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        Cliente cliente = clientePessoaFisicaRepository.findById(id);

        if (cliente == null || !(cliente instanceof ClientePessoaFisica)) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }
        
        if (!cliente.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("clienteEhCorrentista", null, null));
        } 
        
        for (Endereco endereco: cliente.getEnderecos()) {
            enderecoRepository.delete(endereco);
        }
        
        clientePessoaFisicaRepository.delete((ClientePessoaFisica) cliente);
    }

    @Override
    public List<ClientePessoaFisica> findAll() {
        return clientePessoaFisicaRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }    

    @Override
    public ClientePessoaFisica findById(long id) throws NotFoundException {
        Cliente cliente = clientePessoaFisicaRepository.findById(id);

        if (cliente == null || !(cliente instanceof ClientePessoaFisica)) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }        
        
        return (ClientePessoaFisica) cliente;
    }

    @Override
    public ClientePessoaFisica save(ClientePessoaFisica clientePessoaFisica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);
        new ValidationUtil().validateCidades(clientePessoaFisica, cidadeRepository, messageSource);
        
        if (clientePessoaFisica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }
        
        ClientePessoaFisica savedClientePessoaFisica = clientePessoaFisicaRepository.save(clientePessoaFisica);
        
        for (Endereco endereco: clientePessoaFisica.getEnderecos()) {
            endereco.setCliente(clientePessoaFisica);
            enderecoRepository.save(endereco);
        }

        return savedClientePessoaFisica;
    }

    @Override
    public ClientePessoaFisica update(long id, ClientePessoaFisica clientePessoaFisica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);   
        new ValidationUtil().validateCidades(clientePessoaFisica, cidadeRepository, messageSource);
        
        if (clientePessoaFisica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }
        
        ClientePessoaFisica clientePessoaFisicaToUpdate = findById(id);

        if (clientePessoaFisicaToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }
        
        clientePessoaFisicaToUpdate.setNome(clientePessoaFisica.getNome());
        clientePessoaFisicaToUpdate.setStatus(clientePessoaFisica.getStatus());
        clientePessoaFisicaToUpdate.setRg(clientePessoaFisica.getRg());
        clientePessoaFisicaToUpdate.setCpf(clientePessoaFisica.getCpf());
        
        List<Endereco> enderecosToRemove = new ArrayList<>();
        
        for (Endereco endereco: clientePessoaFisicaToUpdate.getEnderecos()) {
            enderecosToRemove.add(endereco);
            enderecoRepository.delete(endereco);
        }
        
        for (Endereco endereco: enderecosToRemove) {
            clientePessoaFisicaToUpdate.getEnderecos().remove(endereco);
        }
        
        for (Endereco endereco: clientePessoaFisica.getEnderecos()) {
            endereco.setCliente(clientePessoaFisicaToUpdate);
            clientePessoaFisicaToUpdate.getEnderecos().add(endereco);
            enderecoRepository.save(endereco);
        }
        
        return clientePessoaFisicaRepository.save(clientePessoaFisicaToUpdate);
    }
}
