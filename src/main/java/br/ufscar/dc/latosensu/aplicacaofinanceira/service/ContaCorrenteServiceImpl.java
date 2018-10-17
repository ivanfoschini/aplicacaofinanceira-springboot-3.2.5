package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Conta;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaCorrente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.AgenciaRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ContaCorrenteRepository;
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
public class ContaCorrenteServiceImpl implements ContaCorrenteService {
    
    @Autowired
    private AgenciaRepository agenciaRepository;
    
    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private MessageSource messageSource;
    
    @Override
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        Conta conta = contaCorrenteRepository.findById(id);

        if (conta == null || !(conta instanceof ContaCorrente)) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }
        
        if (!conta.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("contaPossuiCorrentista", null, null));
        }
        
        contaCorrenteRepository.delete((ContaCorrente) conta);
    }
    
    @Override
    public List<ContaCorrente> findAll() {
        return contaCorrenteRepository.findAll(new Sort(Sort.Direction.ASC, "numero"));
    }    

    @Override
    public ContaCorrente findById(long id) throws NotFoundException {
        Conta conta = contaCorrenteRepository.findById(id);

        if (conta == null || !(conta instanceof ContaCorrente)) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }        
        
        return (ContaCorrente) conta;
    }
    
    @Override
    public ContaCorrente save(ContaCorrente contaCorrente, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);
        validateAgencia(contaCorrente);
        
        if (!isNumberUnique(contaCorrente.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        return contaCorrenteRepository.save(contaCorrente);
    }

    @Override
    public ContaCorrente update(long id, ContaCorrente contaCorrente, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);
        validateAgencia(contaCorrente);
        
        ContaCorrente contaCorrenteToUpdate = findById(id);

        if (contaCorrenteToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }
        
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
        
        return conta == null ? true : false;
    }
    
    private boolean isNumberUnique(Integer numero, Long id) {
        Conta conta = contaCorrenteRepository.findByNumeroAndDifferentId(numero, id);
        
        return conta == null ? true : false;
    } 
    
    private void validateAgencia(ContaCorrente contaCorrente) throws NotFoundException {
        Agencia agencia = agenciaRepository.findById(contaCorrente.getAgencia().getId().longValue());
        
        if (agencia == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }
    }
}
