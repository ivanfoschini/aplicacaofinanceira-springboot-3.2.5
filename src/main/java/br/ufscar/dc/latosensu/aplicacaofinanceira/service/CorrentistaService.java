package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.ClienteTitularidadeDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.CorrentistaDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.MoreThanOneAccountClientException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.MoreThanOneAccountOwnershipException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NoAccountOwnershipException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cliente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Conta;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Correntista;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.CorrentistaPK;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.CorrentistaRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CorrentistaService {

    @Autowired
    private CorrentistaRepository correntistaRepository;

    @Autowired
    private ContaService contaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MessageSource messageSource;

    @Transactional
    public List<Correntista> associate(CorrentistaDTO correntistaDTO) throws HttpMessageNotReadableException, MoreThanOneAccountClientException, MoreThanOneAccountOwnershipException, NoAccountOwnershipException, NotFoundException {
        List<Correntista> correntistas = new ArrayList<>();
        Conta conta = contaService.findById(correntistaDTO.getContaId());

        for (ClienteTitularidadeDTO clienteTitularidadeDTO: correntistaDTO.getClientes()) {
            Cliente cliente = clienteService.findById(clienteTitularidadeDTO.getClienteId());

            Correntista correntista = new Correntista(new CorrentistaPK(conta.getId(), cliente.getId()), clienteTitularidadeDTO.isTitularidade(), conta, cliente);

            correntistas.add(correntista);
        }

        if (hasMoreThanOneAccountOwnership(correntistas)) throw new MoreThanOneAccountOwnershipException(messageSource.getMessage("correntistaTitularidadeDuplicada", null, null));
        if (hasNoAccountOwnership(correntistas)) throw new NoAccountOwnershipException(messageSource.getMessage("correntistaSemTitularidade", null, null));
        if (hasDuplicatedAccountClient(correntistas)) throw new MoreThanOneAccountClientException(messageSource.getMessage("correntistaClienteDuplicado", null, null));

        correntistaRepository.deleteByConta(conta);

        for (Correntista correntista : correntistas) {
            correntistaRepository.save(correntista);
        }

        return correntistas;
    }
    
    public List<Correntista> listByCliente(Long clienteId) {
        return correntistaRepository.findByCliente(clienteId);
    }
    
    public List<Correntista> listByConta(Long contaId) {
        return correntistaRepository.findByConta(contaId);
    }

    private boolean hasDuplicatedAccountClient(List<Correntista> correntistas) {
        List<Cliente> clientes = new ArrayList<>();

        for (Correntista correntista : correntistas) {            
            Cliente cliente = correntista.getCliente();
            
            if (clientes.contains(cliente)) {
                return true;
            } else {
                clientes.add(cliente);
            } 
        }

        return false;
    }

    private boolean hasMoreThanOneAccountOwnership(List<Correntista> correntistas) {
        List<Cliente> titulares = new ArrayList<>();

        for (Correntista correntista : correntistas) {
            if (correntista.getTitularidade()) {
                titulares.add(correntista.getCliente());
            }
        }

        return titulares.size() > 1;
    }

    private boolean hasNoAccountOwnership(List<Correntista> correntistas) {
        List<Cliente> titulares = new ArrayList<>();

        for (Correntista correntista : correntistas) {
            if (correntista.getTitularidade()) {
                titulares.add(correntista.getCliente());
            }
        }

        return titulares.isEmpty();
    }
}
