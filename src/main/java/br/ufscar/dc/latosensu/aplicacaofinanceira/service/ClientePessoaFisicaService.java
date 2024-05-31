package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaFisica;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Endereco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ClientePessoaFisicaRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.EnderecoRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientePessoaFisicaService {

    @Autowired
    private CidadeService cidadeService;
    
    @Autowired
    private ClientePessoaFisicaRepository clientePessoaFisicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private MessageSource messageSource;

    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        ClientePessoaFisica clientePessoaFisica = findById(id);

        if (!clientePessoaFisica.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("clienteEhCorrentista", null, null));
        }

        for (Endereco endereco: clientePessoaFisica.getEnderecos()) {
            enderecoRepository.delete(endereco);
        }
        
        clientePessoaFisicaRepository.delete(clientePessoaFisica);
    }

    public List<ClientePessoaFisica> findAll() {
        List<ClientePessoaFisica> clientesPessoasFisicas = clientePessoaFisicaRepository.findAll(Sort.by("nome"));

        return clientesPessoasFisicas;
    }    

    public ClientePessoaFisica findById(long id) throws NotFoundException {
        return clientePessoaFisicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null)));
    }

    public ClientePessoaFisica save(ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException, NotFoundException {
        if (clientePessoaFisica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }

        cidadeService.validateCidades(clientePessoaFisica.getEnderecos());

        ClientePessoaFisica savedClientePessoaFisica = clientePessoaFisicaRepository.save(clientePessoaFisica);
        
        for (Endereco endereco: clientePessoaFisica.getEnderecos()) {
            endereco.setCliente(clientePessoaFisica);
            enderecoRepository.save(endereco);
        }

        return savedClientePessoaFisica;
    }

    public ClientePessoaFisica update(long id, ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException, NotFoundException {
        if (clientePessoaFisica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }

        cidadeService.validateCidades(clientePessoaFisica.getEnderecos());
        
        ClientePessoaFisica clientePessoaFisicaToUpdate = findById(id);

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
