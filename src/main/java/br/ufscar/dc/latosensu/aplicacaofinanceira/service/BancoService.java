package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.BancoRepository;
import java.util.List;
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
    public void delete(long id) throws NotFoundException {
        Banco banco = findById(id);

        bancoRepository.delete(banco);
    }

    public List<Banco> findAll() {
        List<Banco> bancos = bancoRepository.findAll(Sort.by("nome"));

        return bancos;
    }

    public Banco findById(long id) throws NotFoundException {
        return bancoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("bancoNaoEncontrado", null, null)));
    }

    @Transactional
    public Banco save(Banco banco) throws NotUniqueException {
        if (bancoRepository.findByNumero(banco.getNumero()) != null) {
            throw new NotUniqueException(messageSource.getMessage("bancoNumeroDeveSerUnico", null, null));
        }

        return bancoRepository.save(banco);
    }

    @Transactional
    public Banco update(long id, Banco banco) throws NotFoundException, NotUniqueException {
        Banco bancoToUpdate = findById(id);

        if (bancoRepository.findByNumeroAndDifferentId(banco.getNumero(), bancoToUpdate.getId()) != null) {
            throw new NotUniqueException(messageSource.getMessage("bancoNumeroDeveSerUnico", null, null));
        }

        bancoToUpdate.setNumero(banco.getNumero());
        bancoToUpdate.setCnpj(banco.getCnpj());
        bancoToUpdate.setNome(banco.getNome());

        return bancoRepository.save(bancoToUpdate);
    }
}