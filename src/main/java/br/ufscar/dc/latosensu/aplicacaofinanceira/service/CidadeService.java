package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Endereco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Estado;
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
        return cidadeRepository.findAll(Sort.by("nome"));
    }    

    public Cidade findById(long id) throws NotFoundException {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null)));
    }

    public Cidade save(Cidade cidade) throws NotFoundException, NotUniqueException {
        validateEstado(cidade.getEstado());
        
        if (!isNomeUniqueForEstado(cidade.getNome(), cidade.getEstado().getId())) {
            throw new NotUniqueException(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null));
        }

        return cidadeRepository.save(cidade);
    }

    public Cidade update(long id, Cidade cidade) throws NotFoundException, NotUniqueException {
        validateEstado(cidade.getEstado());
        
        Cidade cidadeToUpdate = findById(id);

        if (!isNomeUniqueForEstado(cidade.getNome(), cidade.getEstado().getId(), cidadeToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null));
        }

        cidadeToUpdate.setNome(cidade.getNome());
        cidadeToUpdate.setEstado(cidade.getEstado());

        return cidadeRepository.save(cidadeToUpdate);
    }

    public void validateCidades(List<Endereco> enderecos) throws NotFoundException {
        for (Endereco endereco: enderecos) {
            if (endereco.getCidade() != null) {
                findById(endereco.getCidade().getId());
            } else {
                throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
            }
        }
    }
    
    private boolean isNomeUniqueForEstado(String nomeDaCidade, Long idDoEstado) {        
        Cidade cidade = cidadeRepository.findByNomeAndEstado(nomeDaCidade, idDoEstado);

        return cidade == null;
    }

    private boolean isNomeUniqueForEstado(String nomeDaCidade, Long idDoEstadoToUpdate, Long idDaCidadeCurrent) {
        Cidade cidade = cidadeRepository.findByNomeAndEstadoAndDifferentId(nomeDaCidade, idDoEstadoToUpdate, idDaCidadeCurrent);

        return cidade == null;
    }

    private void validateEstado(Estado estado) throws NotFoundException {
        estadoService.findById(estado.getId());
    }
}
