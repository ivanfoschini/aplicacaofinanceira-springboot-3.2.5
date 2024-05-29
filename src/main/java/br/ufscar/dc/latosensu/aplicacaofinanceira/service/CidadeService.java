package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.CidadeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private MessageSource messageSource;

    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        Cidade cidade = findById(id);

        if (!cidade.getEnderecos().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("cidadePossuiEnderecos", null, null));
        }

        cidadeRepository.delete(cidade);
    }

    public List<Cidade> findAll() {
        List<Cidade> cidades = cidadeRepository.findAll(Sort.by("nome"));

        return cidades;
    }

    public Cidade findById(long id) throws NotFoundException {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null)));
    }

    public Cidade save(Cidade cidade) throws NotFoundException, NotUniqueException {
        estadoService.findById(cidade.getEstado().getId());

        if (cidadeRepository.findByNomeAndEstado(cidade.getNome(), cidade.getEstado().getId()) != null) {
            throw new NotUniqueException(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null));
        }

        return cidadeRepository.save(cidade);
    }

    public Cidade update(long id, Cidade cidade) throws NotFoundException, NotUniqueException {
        estadoService.findById(cidade.getEstado().getId());

        Cidade cidadeToUpdate = findById(id);

        if (cidadeRepository.findByNomeAndEstadoAndDifferentId(cidade.getNome(), cidade.getEstado().getId(), cidadeToUpdate.getId()) != null) {
            throw new NotUniqueException(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null));
        }

        cidadeToUpdate.setNome(cidade.getNome());
        cidadeToUpdate.setEstado(cidade.getEstado());

        return cidadeRepository.save(cidadeToUpdate);
    }
}