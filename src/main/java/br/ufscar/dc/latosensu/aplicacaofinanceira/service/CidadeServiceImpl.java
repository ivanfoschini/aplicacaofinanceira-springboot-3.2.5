package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Estado;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.CidadeRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.EstadoRepository;
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
public class CidadeServiceImpl implements CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        Cidade cidade = cidadeRepository.findById(id);

        if (cidade == null) {
            throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
        }
        
        if (!cidade.getEnderecos().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("cidadePossuiEnderecos", null, null));
        }
        
        cidadeRepository.delete(cidade);
    }

    @Override
    public List<Cidade> findAll() {
        return cidadeRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }    

    @Override
    public Cidade findById(long id) throws NotFoundException {
        Cidade cidade = cidadeRepository.findById(id);

        if (cidade == null) {
            throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
        }        
        
        return cidade;
    }

    @Override
    public Cidade save(Cidade cidade, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);               
        
        validateEstado(cidade);
        
        if (!isNomeUniqueForEstado(cidade.getNome(), cidade.getEstado().getId())) {
            throw new NotUniqueException(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null));
        }

        return cidadeRepository.save(cidade);
    }

    @Override
    public Cidade update(long id, Cidade cidade, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);   
        
        validateEstado(cidade);
        
        Cidade cidadeToUpdate = findById(id);

        if (cidadeToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
        }
        
        if (!isNomeUniqueForEstado(cidade.getNome(), cidade.getEstado().getId(), cidadeToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null));
        }

        cidadeToUpdate.setNome(cidade.getNome());
        cidadeToUpdate.setEstado(cidade.getEstado());

        return cidadeRepository.save(cidadeToUpdate);
    } 
    
    private boolean isNomeUniqueForEstado(String nomeDaCidade, Long idDoEstado) {        
        Cidade cidade = cidadeRepository.findByNomeAndEstado(nomeDaCidade, idDoEstado);

        return cidade != null ? false : true;        
    }

    private boolean isNomeUniqueForEstado(String nomeDaCidade, Long idDoEstadoToUpdate, Long idDaCidadeCurrent) {
        Cidade cidade = cidadeRepository.findByNomeAndEstadoAndDifferentId(nomeDaCidade, idDoEstadoToUpdate, idDaCidadeCurrent);

        return cidade != null ? false : true;
    }
    
    private void validateEstado(Cidade cidade) throws NotFoundException {
        Estado estado = estadoRepository.findById(cidade.getEstado().getId().longValue());
        
        if (estado == null) {
            throw new NotFoundException(messageSource.getMessage("estadoNaoEncontrado", null, null));
        }
    } 
}
