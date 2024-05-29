package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.dto.EstadoDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Estado;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.EstadoRepository;
import java.util.List;

import org.springframework.beans.BeanUtils;
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

    public Estado save(EstadoDTO estadoDTO) throws NotUniqueException {
        Estado estado = estadoRepository.findByNome(estadoDTO.getNome());

        if (estado != null) {
            throw new NotUniqueException(messageSource.getMessage("estadoNomeDeveSerUnico", null, null));
        }

        estado = new Estado();

        BeanUtils.copyProperties(estadoDTO, estado);

        return estadoRepository.save(estado);
    }

    public Estado update(long id, EstadoDTO estadoDTO) throws NotFoundException, NotUniqueException {
        Estado estado = estadoRepository.findByNomeAndDifferentId(estadoDTO.getNome(), id);

        if (estado != null && !estado.getId().equals(id)) {
            throw new NotUniqueException(messageSource.getMessage("estadoNomeDeveSerUnico", null, null));
        }

        estado = findById(id);

        BeanUtils.copyProperties(estadoDTO, estado);

        return estadoRepository.save(estado);
    }
}
