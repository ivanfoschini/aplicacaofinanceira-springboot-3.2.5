package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.AgenciaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private BancoService bancoService;

    @Autowired
    private CidadeService cidadeService;

    @Autowired
    private MessageSource messageSource;

    public void delete(long id) throws NotEmptyCollectionException, NotFoundException {
        Agencia agencia = findById(id);

        if (!agencia.getContas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("agenciaPossuiContas", null, null));
        }

        agenciaRepository.delete(agencia);
    }

    public List<Agencia> findAll() {
        List<Agencia> agencias = agenciaRepository.findAll(Sort.by("nome"));

        return agencias;
    }

    public Agencia findById(long id) throws NotFoundException {
        return agenciaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null)));
    }

    public Agencia save(Agencia agencia) throws NotFoundException, NotUniqueException {
        bancoService.findById(agencia.getBanco().getId());
        cidadeService.findById(agencia.getEndereco().getCidade().getId());

        if (agenciaRepository.findByNumero(agencia.getNumero()) != null) {
            throw new NotUniqueException(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null));
        }

        return agenciaRepository.save(agencia);
    }

    public Agencia update(long id, Agencia agencia) throws NotFoundException, NotUniqueException {
        bancoService.findById(agencia.getBanco().getId());
        cidadeService.findById(agencia.getEndereco().getCidade().getId());

        Agencia agenciaToUpdate = findById(id);

        if (agenciaToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }

        if (agenciaRepository.findByNumeroAndDifferentId(agencia.getNumero(), agenciaToUpdate.getId()) != null) {
            throw new NotUniqueException(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null));
        }

        agenciaToUpdate.setNome(agencia.getNome());
        agenciaToUpdate.setNumero(agencia.getNumero());
        agenciaToUpdate.setBanco(agencia.getBanco());
        agenciaToUpdate.getEndereco().setLogradouro(agencia.getEndereco().getLogradouro());
        agenciaToUpdate.getEndereco().setNumero(agencia.getEndereco().getNumero());
        agenciaToUpdate.getEndereco().setComplemento(agencia.getEndereco().getComplemento());
        agenciaToUpdate.getEndereco().setBairro(agencia.getEndereco().getBairro());
        agenciaToUpdate.getEndereco().setCep(agencia.getEndereco().getCep());
        agenciaToUpdate.getEndereco().setCidade(agencia.getEndereco().getCidade());

        return agenciaRepository.save(agenciaToUpdate);
    }
}