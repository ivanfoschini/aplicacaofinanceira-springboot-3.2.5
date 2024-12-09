package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaCorrente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ContaCorrenteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContaCorrenteService {
    
    @Autowired
    private AgenciaService agenciaService;
    
    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private MessageSource messageSource;

    @Transactional
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        ContaCorrente contaCorrente = findById(id);

        if (!contaCorrente.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("contaPossuiCorrentista", null, null));
        }

        contaCorrenteRepository.delete(contaCorrente);
    }
    
    public List<ContaCorrente> findAll() {
        List<ContaCorrente> contasCorrentes = contaCorrenteRepository.findAll(Sort.by("numero"));

        return contasCorrentes;
    }    

    public ContaCorrente findById(long id) throws NotFoundException {
        return contaCorrenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null)));
    }

    @Transactional
    public ContaCorrente save(ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException {
        agenciaService.findById(contaCorrente.getAgencia().getId());
        
        if (contaCorrenteRepository.findByNumero(contaCorrente.getNumero()) != null) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        return contaCorrenteRepository.save(contaCorrente);
    }

    @Transactional
    public ContaCorrente update(long id, ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException {
        agenciaService.findById(contaCorrente.getAgencia().getId());
        
        ContaCorrente contaCorrenteToUpdate = findById(id);

        if (contaCorrenteRepository.findByNumeroAndDifferentId(contaCorrente.getNumero(), contaCorrenteToUpdate.getId()) != null) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        contaCorrenteToUpdate.setNumero(contaCorrente.getNumero());
        contaCorrenteToUpdate.setSaldo(contaCorrente.getSaldo());
        contaCorrenteToUpdate.setDataDeAbertura(contaCorrente.getDataDeAbertura());
        contaCorrenteToUpdate.setLimite(contaCorrente.getLimite());
        contaCorrenteToUpdate.setAgencia(contaCorrente.getAgencia());

        return contaCorrenteRepository.save(contaCorrenteToUpdate);
    }
}
