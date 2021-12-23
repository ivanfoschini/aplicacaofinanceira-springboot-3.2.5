package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cliente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MessageSource messageSource;

    public Cliente findById(long id) throws NotFoundException {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null)));
    }
}
