package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaJuridica;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Endereco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ClientePessoaJuridicaRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.EnderecoRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientePessoaJuridicaService {

    @Autowired
    private CidadeService cidadeService;
    
    @Autowired
    private ClientePessoaJuridicaRepository clientePessoaJuridicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Transactional
    public void delete(long id) throws NotFoundException {
        ClientePessoaJuridica clientePessoaJuridica = findById(id);

        for (Endereco endereco: clientePessoaJuridica.getEnderecos()) {
            enderecoRepository.delete(endereco);
        }
        
        clientePessoaJuridicaRepository.delete(clientePessoaJuridica);
    }

    public List<ClientePessoaJuridica> findAll() {
        List<ClientePessoaJuridica> clientesPessoasJuridicas = clientePessoaJuridicaRepository.findAll(Sort.by("nome"));

        return clientesPessoasJuridicas;
    }    

    public ClientePessoaJuridica findById(long id) throws NotFoundException {
        return clientePessoaJuridicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null)));
    }

    @Transactional
    public ClientePessoaJuridica save(ClientePessoaJuridica clientePessoaJuridica) throws EmptyCollectionException, NotFoundException {
        if (clientePessoaJuridica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }

        cidadeService.validateCidades(clientePessoaJuridica.getEnderecos());
        
        ClientePessoaJuridica savedClientePessoaJuridica = clientePessoaJuridicaRepository.save(clientePessoaJuridica);
        
        for (Endereco endereco: clientePessoaJuridica.getEnderecos()) {
            endereco.setCliente(clientePessoaJuridica);
            enderecoRepository.save(endereco);
        }

        return savedClientePessoaJuridica;
    }

    @Transactional
    public ClientePessoaJuridica update(long id, ClientePessoaJuridica clientePessoaJuridica) throws EmptyCollectionException, NotFoundException {
        if (clientePessoaJuridica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }

        cidadeService.validateCidades(clientePessoaJuridica.getEnderecos());
        
        ClientePessoaJuridica clientePessoaJuridicaToUpdate = findById(id);

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
