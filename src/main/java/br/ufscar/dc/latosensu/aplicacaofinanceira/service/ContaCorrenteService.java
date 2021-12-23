package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Conta;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaCorrente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ContaCorrenteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContaCorrenteService {
    
    @Autowired
    private AgenciaService agenciaService;
    
    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private MessageSource messageSource;
    
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        ContaCorrente contaCorrente = findById(id);

        if (!contaCorrente.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("contaPossuiCorrentista", null, null));
        }

        contaCorrenteRepository.delete(contaCorrente);
    }
    
    public List<ContaCorrente> findAll() {
        return contaCorrenteRepository.findAll(Sort.by("numero"));
    }    

    public ContaCorrente findById(long id) throws NotFoundException {
        return contaCorrenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null)));
    }
    
    public ContaCorrente save(ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException {
        validateAgencia(contaCorrente.getAgencia());
        
        if (!isNumberUnique(contaCorrente.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        return contaCorrenteRepository.save(contaCorrente);
    }

    public ContaCorrente update(long id, ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException {
        validateAgencia(contaCorrente.getAgencia());
        
        ContaCorrente contaCorrenteToUpdate = findById(id);

        if (!isNumberUnique(contaCorrente.getNumero(), contaCorrenteToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        contaCorrenteToUpdate.setNumero(contaCorrente.getNumero());
        contaCorrenteToUpdate.setSaldo(contaCorrente.getSaldo());
        contaCorrenteToUpdate.setDataDeAbertura(contaCorrente.getDataDeAbertura());
        contaCorrenteToUpdate.setLimite(contaCorrente.getLimite());
        contaCorrenteToUpdate.setAgencia(contaCorrente.getAgencia());

        return contaCorrenteRepository.save(contaCorrenteToUpdate);
    }
    
    private boolean isNumberUnique(Integer numero) {
        Conta conta = contaCorrenteRepository.findByNumero(numero);
        
        return conta == null;
    }
    
    private boolean isNumberUnique(Integer numero, Long id) {
        Conta conta = contaCorrenteRepository.findByNumeroAndDifferentId(numero, id);
        
        return conta == null;
    } 
    
    private void validateAgencia(Agencia agencia) throws NotFoundException {
        agenciaService.findById(agencia.getId());
    }
}
