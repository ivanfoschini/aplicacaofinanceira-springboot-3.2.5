package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.CorrentistaDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.DifferentAccountsException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.MoreThanOneAccountClientException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.MoreThanOneAccountOwnershipException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NoAccountOwnershipException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cliente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Conta;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Correntista;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.CorrentistaPK;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ClienteRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ContaRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.CorrentistaRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CorrentistaServiceImpl implements CorrentistaService {

    @Autowired
    private CorrentistaRepository correntistaRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<Correntista> associate(List<CorrentistaDTO> correntistasDTO) throws DifferentAccountsException, HttpMessageNotReadableException, MoreThanOneAccountClientException, MoreThanOneAccountOwnershipException, NoAccountOwnershipException, NotFoundException {
        if (correntistasDTO.isEmpty()) throw new HttpMessageNotReadableException(messageSource.getMessage("generalBadRequest", null, null));
        
        List<Correntista> correntistas = new ArrayList<>();
        Conta conta = null;

        for (CorrentistaDTO correntistaJson : correntistasDTO) {
            Long contaId = correntistaJson.getContaId();

            conta = contaRepository.findById(contaId.longValue());

            if (conta == null) throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
            
            Long clienteId = correntistaJson.getClienteId();

            Cliente cliente = clienteRepository.findById(clienteId.longValue());

            if (cliente == null) throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));            

            Correntista correntista = new Correntista(new CorrentistaPK(contaId, clienteId), correntistaJson.isTitularidade(), conta, cliente);
            
            correntistas.add(correntista);
        }

        if (hasMoreThanOneAccountOwnership(correntistas)) throw new MoreThanOneAccountOwnershipException(messageSource.getMessage("correntistaTitularidadeDuplicada", null, null));
        if (hasNoAccountOwnership(correntistas)) throw new NoAccountOwnershipException(messageSource.getMessage("correntistaSemTitularidade", null, null));
        if (hasDifferentAccounts(correntistas)) throw new DifferentAccountsException(messageSource.getMessage("correntistaContasDiferentes", null, null));
        if (hasDuplicatedAccountClient(correntistas)) throw new MoreThanOneAccountClientException(messageSource.getMessage("correntistaClienteDuplicado", null, null));
                
        correntistaRepository.deleteByConta(conta);

        for (Correntista correntista : correntistas) {
            correntistaRepository.save(correntista);
        }

        return correntistas;
    }
    
    @Override
    public List<Correntista> listByCliente(Long clienteId) {
        return correntistaRepository.findByCliente(clienteId);
    }
    
    @Override
    public List<Correntista> listByConta(Long contaId) {
        return correntistaRepository.findByConta(contaId);
    }

    private boolean hasDifferentAccounts(List<Correntista> correntistas) {
        Set<Long> contasIds = new HashSet<>();

        for (Correntista correntista : correntistas) {
            contasIds.add(correntista.getConta().getId());            
        }
        
        return contasIds.size() > 1 ? true : false;
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

        return titulares.size() > 1 ? true : false;
    }

    private boolean hasNoAccountOwnership(List<Correntista> correntistas) {
        List<Cliente> titulares = new ArrayList<>();

        for (Correntista correntista : correntistas) {
            if (correntista.getTitularidade()) {
                titulares.add(correntista.getCliente());
            }
        }

        return titulares.isEmpty() ? true : false;
    }
}
