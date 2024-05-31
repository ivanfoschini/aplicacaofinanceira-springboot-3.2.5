package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaPoupanca;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ContaPoupancaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContaPoupancaService {
    
    @Autowired
    private AgenciaService agenciaService;
    
    @Autowired
    private ContaPoupancaRepository contaPoupancaRepository;

    @Autowired
    private MessageSource messageSource;
    
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        ContaPoupanca contaPoupanca = findById(id);

        if (!contaPoupanca.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("contaPossuiCorrentista", null, null));
        }

        contaPoupancaRepository.delete(contaPoupanca);
    }
    
    public List<ContaPoupanca> findAll() {
        List<ContaPoupanca> contasPoupancas = contaPoupancaRepository.findAll(Sort.by("numero"));

        return contasPoupancas;
    }    

    public ContaPoupanca findById(long id) throws NotFoundException {
        return contaPoupancaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null)));
    }
    
    public ContaPoupanca save(ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException {
        agenciaService.findById(contaPoupanca.getAgencia().getId());
        
        if (contaPoupancaRepository.findByNumero(contaPoupanca.getNumero()) != null) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        return contaPoupancaRepository.save(contaPoupanca);
    }

    public ContaPoupanca update(long id, ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException {
        agenciaService.findById(contaPoupanca.getAgencia().getId());
        
        ContaPoupanca contaPoupancaToUpdate = findById(id);

        if (contaPoupancaRepository.findByNumeroAndDifferentId(contaPoupanca.getNumero(), contaPoupancaToUpdate.getId()) != null) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        contaPoupancaToUpdate.setNumero(contaPoupanca.getNumero());
        contaPoupancaToUpdate.setSaldo(contaPoupanca.getSaldo());
        contaPoupancaToUpdate.setDataDeAbertura(contaPoupanca.getDataDeAbertura());
        contaPoupancaToUpdate.setDataDeAniversario(contaPoupanca.getDataDeAniversario());
        contaPoupancaToUpdate.setCorrecaoMonetaria(contaPoupanca.getCorrecaoMonetaria());
        contaPoupancaToUpdate.setJuros(contaPoupanca.getJuros());
        contaPoupancaToUpdate.setAgencia(contaPoupanca.getAgencia());

        return contaPoupancaRepository.save(contaPoupancaToUpdate);
    }
}
