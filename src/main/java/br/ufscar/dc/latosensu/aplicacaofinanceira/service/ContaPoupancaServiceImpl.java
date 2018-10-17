package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Conta;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaPoupanca;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.AgenciaRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ContaPoupancaRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.validation.ValidationUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional
public class ContaPoupancaServiceImpl implements ContaPoupancaService {
    
    @Autowired
    private AgenciaRepository agenciaRepository;
    
    @Autowired
    private ContaPoupancaRepository contaPoupancaRepository;

    @Autowired
    private MessageSource messageSource;
    
    @Override
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        Conta conta = contaPoupancaRepository.findById(id);

        if (conta == null || !(conta instanceof ContaPoupanca)) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }
        
        if (!conta.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("contaPossuiCorrentista", null, null));
        }
        
        contaPoupancaRepository.delete((ContaPoupanca) conta);
    }
    
    @Override
    public List<ContaPoupanca> findAll() {
        return contaPoupancaRepository.findAll(new Sort(Sort.Direction.ASC, "numero"));
    }    

    @Override
    public ContaPoupanca findById(long id) throws NotFoundException {
        Conta conta = contaPoupancaRepository.findById(id);

        if (conta == null || !(conta instanceof ContaPoupanca)) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }        
        
        return (ContaPoupanca) conta;
    }
    
    @Override
    public ContaPoupanca save(ContaPoupanca contaPoupanca, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);
        validateAgencia(contaPoupanca);
        
        if (!isNumberUnique(contaPoupanca.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        return contaPoupancaRepository.save(contaPoupanca);
    }

    @Override
    public ContaPoupanca update(long id, ContaPoupanca contaPoupanca, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);
        validateAgencia(contaPoupanca);
        
        ContaPoupanca contaPoupancaToUpdate = findById(id);

        if (contaPoupancaToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }
        
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
        
        return conta == null ? true : false;
    }
    
    private boolean isNumberUnique(Integer numero, Long id) {
        Conta conta = contaPoupancaRepository.findByNumeroAndDifferentId(numero, id);
        
        return conta == null ? true : false;
    } 
    
    private void validateAgencia(ContaPoupanca contaPoupanca) throws NotFoundException {
        Agencia agencia = agenciaRepository.findById(contaPoupanca.getAgencia().getId().longValue());
        
        if (agencia == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }
    }
}
