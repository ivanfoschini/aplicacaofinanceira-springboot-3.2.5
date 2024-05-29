package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.dto.BancoDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BancoService {

    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Transactional
    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        Banco banco = findById(id);

        if (!banco.getAgencias().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("bancoPossuiAgencias", null, null));
        }

        bancoRepository.delete(banco);
    }

    public List<Banco> findAll() {
        return bancoRepository.findAll(Sort.by("nome"));
    }    

    public Banco findById(long id) throws NotFoundException {
        return bancoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("bancoNaoEncontrado", null, null)));
    }

    @Transactional
    public Banco save(BancoDTO bancoDTO) throws NotUniqueException {
        Banco banco = bancoRepository.findByNumero(bancoDTO.getNumero());

        if (banco != null) {
            throw new NotUniqueException(messageSource.getMessage("bancoNumeroDeveSerUnico", null, null));
        }

        banco = new Banco();

        BeanUtils.copyProperties(bancoDTO, banco);

        return bancoRepository.save(banco);
    }

    @Transactional
    public Banco update(long id, BancoDTO bancoDTO) throws NotFoundException, NotUniqueException {
        Banco banco = bancoRepository.findByNumeroAndDifferentId(bancoDTO.getNumero(), id);

        if (banco != null && !banco.getId().equals(id)) {
            throw new NotUniqueException(messageSource.getMessage("bancoNumeroDeveSerUnico", null, null));
        }

        banco = findById(id);

        BeanUtils.copyProperties(bancoDTO, banco);

        return bancoRepository.save(banco);
    }
}
