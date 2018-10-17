package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.AgenciaRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.CidadeRepository;
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
    public Agencia save(Agencia agencia, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validateAgenciaAndEndereco(agencia, bindingResult);
        validateBanco(agencia);
        validateCidade(agencia);        
        
        if (!isNumberUnique(agencia.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null));
        }

        return agenciaRepository.save(agencia);
    }

    @Override
    public Agencia update(long id, Agencia agencia, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validateAgenciaAndEndereco(agencia, bindingResult);   
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
}
