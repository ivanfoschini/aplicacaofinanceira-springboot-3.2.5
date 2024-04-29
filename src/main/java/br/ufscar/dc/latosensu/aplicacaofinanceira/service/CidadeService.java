package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.dto.CidadeDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.CidadeRepository;
import java.util.List;

import org.springframework.beans.BeanUtils;
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

    public void delete(long id) throws NotFoundException {
        Cidade cidade = findById(id);

        cidadeRepository.delete(cidade);
    }

    public List<Cidade> findAll() {
        return cidadeRepository.findAll(Sort.by("nome"));
    }    

    public Cidade findById(long id) throws NotFoundException {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null)));
    }

    public Cidade save(CidadeDTO cidadeDTO) throws NotFoundException, NotUniqueException {
        estadoService.findById(cidadeDTO.getEstado().getId());

        Cidade cidade = cidadeRepository.findByNomeAndEstado(cidadeDTO.getNome(), cidadeDTO.getEstado().getId());
        
        if (cidade != null) {
            throw new NotUniqueException(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null));
        }

        cidade = new Cidade();

        BeanUtils.copyProperties(cidadeDTO, cidade);

        return cidadeRepository.save(cidade);
    }

    public Cidade update(long id, CidadeDTO cidadeDTO) throws NotFoundException, NotUniqueException {
        estadoService.findById(cidadeDTO.getEstado().getId());

        Cidade cidade = cidadeRepository.findByNomeAndEstadoAndDifferentId(cidadeDTO.getNome(), cidadeDTO.getEstado().getId(), id);
        
        if (cidade != null && !cidade.getId().equals(id)) {
            throw new NotUniqueException(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null));
        }

        cidade = findById(id);

        BeanUtils.copyProperties(cidadeDTO, cidade);

        return cidadeRepository.save(cidade);
    }
}
