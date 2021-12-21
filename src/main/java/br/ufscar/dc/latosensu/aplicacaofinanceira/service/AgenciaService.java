package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

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
    
    public void delete(long id) throws NotFoundException {
        Agencia agencia = findById(id);

        agenciaRepository.delete(agencia);
    }

    public List<Agencia> findAll() {
        return agenciaRepository.findAll(Sort.by("nome"));
    }    

    public Agencia findById(long id) throws NotFoundException {
        return agenciaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null)));
    }

    public Agencia save(Agencia agencia) throws NotFoundException, NotUniqueException {
        validateBanco(agencia);
        validateCidade(agencia);
        
        if (!isNumberUnique(agencia.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null));
        }

        return agenciaRepository.save(agencia);
    }

    public Agencia update(long id, Agencia agencia) throws NotFoundException, NotUniqueException {
        validateBanco(agencia);
        validateCidade(agencia);
        
        Agencia agenciaToUpdate = findById(id);

        if (agenciaToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }
        
        if (!isNumberUnique(agencia.getNumero(), agenciaToUpdate.getId())) {
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
    
    private boolean isNumberUnique(Integer numero) {
        Agencia agencia = agenciaRepository.findByNumero(numero);
        
        return agencia == null;
    }
    
    private boolean isNumberUnique(Integer numero, Long id) {
        Agencia agencia = agenciaRepository.findByNumeroAndDifferentId(numero, id);
        
        return agencia == null;
    }

    private void validateBanco(Agencia agencia) throws NotFoundException {
        bancoService.findById(agencia.getBanco().getId());
    }

    private void validateCidade(Agencia agencia) throws NotFoundException {
        cidadeService.findById(agencia.getEndereco().getCidade().getId());
    }
}
