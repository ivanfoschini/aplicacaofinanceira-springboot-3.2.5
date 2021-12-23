package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Conta;
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
        return contaPoupancaRepository.findAll(Sort.by("numero"));
    }    

    public ContaPoupanca findById(long id) throws NotFoundException {
        return contaPoupancaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null)));
    }
    
    public ContaPoupanca save(ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException {
        validateAgencia(contaPoupanca.getAgencia());
        
        if (!isNumberUnique(contaPoupanca.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        return contaPoupancaRepository.save(contaPoupanca);
    }

    public ContaPoupanca update(long id, ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException {
        validateAgencia(contaPoupanca.getAgencia());
        
        ContaPoupanca contaPoupancaToUpdate = findById(id);

        if (!isNumberUnique(contaPoupanca.getNumero(), contaPoupancaToUpdate.getId())) {
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
    
    private boolean isNumberUnique(Integer numero) {
        Conta conta = contaPoupancaRepository.findByNumero(numero);
        
        return conta == null;
    }
    
    private boolean isNumberUnique(Integer numero, Long id) {
        Conta conta = contaPoupancaRepository.findByNumeroAndDifferentId(numero, id);
        
        return conta == null;
    } 
    
    private void validateAgencia(Agencia agencia) throws NotFoundException {
        agenciaService.findById(agencia.getId());
    }
}
