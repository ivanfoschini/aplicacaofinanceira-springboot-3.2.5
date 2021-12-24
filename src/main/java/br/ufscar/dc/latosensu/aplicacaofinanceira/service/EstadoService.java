package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Estado;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.EstadoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;
    
    @Autowired
    private MessageSource messageSource;

    public void delete(long id) throws NotEmptyCollectionException,  NotFoundException {
        Estado estado = findById(id);

        if (!estado.getCidades().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("estadoPossuiCidades", null, null));
        }

        estadoRepository.delete(estado);
    }

    public List<Estado> findAll() {
        return estadoRepository.findAll(Sort.by("nome"));
    }    

    public Estado findById(long id) throws NotFoundException {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("estadoNaoEncontrado", null, null)));
    }

    public Estado save(Estado estado) throws NotUniqueException {
        if (!isNomeUnique(estado.getNome())) {
            throw new NotUniqueException(messageSource.getMessage("estadoNomeDeveSerUnico", null, null));
        }

        return estadoRepository.save(estado);
    }

    public Estado update(long id, Estado estado) throws NotFoundException, NotUniqueException {
        Estado estadoToUpdate = findById(id);

        if (!isNomeUnique(estado.getNome(), estadoToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("estadoNomeDeveSerUnico", null, null));
        }

        estadoToUpdate.setNome(estado.getNome());

        return estadoRepository.save(estadoToUpdate);
    }

    private boolean isNomeUnique(String nome) {
        Estado estado = estadoRepository.findByNome(nome);
        
        return estado == null;
    }
    
    private boolean isNomeUnique(String nome, Long id) {
        Estado estado = estadoRepository.findByNomeAndDifferentId(nome, id);
        
        return estado == null;
    }
}
