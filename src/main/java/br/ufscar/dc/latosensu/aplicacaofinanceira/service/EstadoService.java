package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

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
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MessageSource messageSource;

    @Transactional
    public void delete(long id) throws NotFoundException {
        Estado estado = findById(id);

        estadoRepository.delete(estado);
    }

    public List<Estado> findAll() {
        List<Estado> estados = estadoRepository.findAll(Sort.by("nome"));

        return estados;
    }

    public Estado findById(long id) throws NotFoundException {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("estadoNaoEncontrado", null, null)));
    }

    @Transactional
    public Estado save(Estado estado) throws NotUniqueException {
        if (estadoRepository.findByNome(estado.getNome()) != null) {
            throw new NotUniqueException(messageSource.getMessage("estadoNomeDeveSerUnico", null, null));
        }

        return estadoRepository.save(estado);
    }

    @Transactional
    public Estado update(long id, Estado estado) throws NotFoundException, NotUniqueException {
        Estado estadoToUpdate = findById(id);

        if (estadoRepository.findByNomeAndDifferentId(estado.getNome(), estadoToUpdate.getId()) != null) {
            throw new NotUniqueException(messageSource.getMessage("estadoNomeDeveSerUnico", null, null));
        }

        estadoToUpdate.setNome(estado.getNome());

        return estadoRepository.save(estadoToUpdate);
    }
}