package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Estado;
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
public class EstadoServiceImpl implements EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        Estado estado = estadoRepository.findById(id);

        if (estado == null) {
            throw new NotFoundException(messageSource.getMessage("estadoNaoEncontrado", null, null));
        }
        
        if (!estado.getCidades().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("estadoPossuiCidades", null, null));
        }
        
        estadoRepository.delete(estado);
    }

    @Override
    public List<Estado> findAll() {
        return estadoRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }    

    @Override
    public Estado findById(long id) throws NotFoundException {
        Estado estado = estadoRepository.findById(id);

        if (estado == null) {
            throw new NotFoundException(messageSource.getMessage("estadoNaoEncontrado", null, null));
        }        
        
        return estado;
    }

    @Override
    public Estado save(Estado estado, BindingResult bindingResult) throws NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);               
        
        if (!isNomeUnique(estado.getNome())) {
            throw new NotUniqueException(messageSource.getMessage("estadoNomeDeveSerUnico", null, null));
        }

        return estadoRepository.save(estado);
    }

    @Override
    public Estado update(long id, Estado estado, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        new ValidationUtil().validate(bindingResult);   
        
        Estado estadoToUpdate = findById(id);

        if (estadoToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("estadoNaoEncontrado", null, null));
        }
        
        if (!isNomeUnique(estado.getNome(), estadoToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("estadoNomeDeveSerUnico", null, null));
        }

        estadoToUpdate.setNome(estado.getNome());

        return estadoRepository.save(estadoToUpdate);
    }

    private boolean isNomeUnique(String nome) {
        Estado estado = estadoRepository.findByNome(nome);
        
        return estado == null ? true : false;
    }
    
    private boolean isNomeUnique(String nome, Long id) {
        Estado estado = estadoRepository.findByNomeAndDifferentId(nome, id);
        
        return estado == null ? true : false;
    }
}
