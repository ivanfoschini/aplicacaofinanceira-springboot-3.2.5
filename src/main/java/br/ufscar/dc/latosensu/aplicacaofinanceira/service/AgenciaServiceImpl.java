package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Endereco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.AgenciaRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.CidadeRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.validation.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AgenciaServiceImpl implements AgenciaService {
    
    @Autowired
    private AgenciaRepository agenciaRepository;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private MessageSource messageSource;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        Agencia agencia = agenciaRepository.findById(id);

        if (agencia == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }
        
        if (!agencia.getContas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("agenciaPossuiContas", null, null));
        }
        
        agenciaRepository.delete(agencia);
    }

    @Override
    public List<Agencia> findAll() {
        return agenciaRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }    

    @Override
    public Agencia findById(long id) throws NotFoundException {
        Agencia agencia = agenciaRepository.findById(id);

        if (agencia == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }        
        
        return agencia;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Agencia save(Agencia agencia, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        validateAgenciaAndEndereco(agencia, bindingResult);
        validateBanco(agencia);
        validateCidade(agencia);        
        
        if (!isNumberUnique(agencia.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null));
        }

        return agenciaRepository.save(agencia);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Agencia update(long id, Agencia agencia, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        validateAgenciaAndEndereco(agencia, bindingResult);   
        validateBanco(agencia);
        validateCidade(agencia);
        
        Agencia agenciaToUpdate = findById(id);

        if (agenciaToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }
        
        if (!isNumberUnique(agencia.getNumero(), agenciaToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null));
        }

        agenciaToUpdate.setNome(agencia.getNome());
        agenciaToUpdate.setNumero(agencia.getNumero());
        agenciaToUpdate.setBanco(agencia.getBanco());
        agenciaToUpdate.getEndereco().setLogradouro(agencia.getEndereco().getLogradouro());
        agenciaToUpdate.getEndereco().setNumero(agencia.getEndereco().getNumero());
        agenciaToUpdate.getEndereco().setComplemento(agencia.getEndereco().getComplemento());
        agenciaToUpdate.getEndereco().setBairro(agencia.getEndereco().getBairro());
        agenciaToUpdate.getEndereco().setCep(agencia.getEndereco().getCep());
        agenciaToUpdate.getEndereco().setCidade(agencia.getEndereco().getCidade());

        return agenciaRepository.save(agenciaToUpdate);
    }
    
    private boolean isNumberUnique(Integer numero) {
        Agencia agencia = agenciaRepository.findByNumero(numero);
        
        return agencia == null ? true : false;
    }
    
    private boolean isNumberUnique(Integer numero, Long id) {
        Agencia agencia = agenciaRepository.findByNumeroAndDifferentId(numero, id);
        
        return agencia == null ? true : false;
    }
    
    private void validateAgenciaAndEndereco(Agencia agencia, BindingResult bindingResult) throws ValidationException {
        List<FieldError> enderecoFieldErrors = validateEndereco(agencia.getEndereco());
        
        if (bindingResult.hasErrors() || !enderecoFieldErrors.isEmpty()) {
            List<FieldError> fieldErrors = new ArrayList<>(); 
            
            fieldErrors.addAll(bindingResult.getFieldErrors());
            fieldErrors.addAll(enderecoFieldErrors);
            
            new ValidationUtil().validate(fieldErrors);
        }
    }

    private void validateBanco(Agencia agencia) throws NotFoundException {
        Banco banco = bancoRepository.findById(agencia.getBanco().getId().longValue());
        
        if (banco == null) {
            throw new NotFoundException(messageSource.getMessage("bancoNaoEncontrado", null, null));
        }
    }

    private void validateCidade(Agencia agencia) throws NotFoundException {
        Cidade cidade = cidadeRepository.findById(agencia.getEndereco().getCidade().getId().longValue());
        
        if (cidade == null) {
            throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
        }
    }
    
    private List<FieldError> validateEndereco(Endereco endereco) {
        Set<ConstraintViolation<Endereco>> enderecoViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(endereco);
        
        List<FieldError> fieldErrors = new ArrayList<>();  
        
        for (ConstraintViolation<Endereco> violation: enderecoViolations) {
            FieldError enderecoFieldError = new FieldError("endereco", "field", violation.getMessage());
            
            fieldErrors.add(enderecoFieldError);
        }
        
        return fieldErrors;
    }
}
