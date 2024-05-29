package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.dto.AgenciaDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Endereco;
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

    public Agencia save(AgenciaDTO agenciaDTO) throws NotFoundException, NotUniqueException {
        Banco banco = bancoService.findById(agenciaDTO.getBancoId());
        Cidade cidade = cidadeService.findById(agenciaDTO.getEndereco().getCidadeId());

        Agencia agencia = agenciaRepository.findByNumero(agenciaDTO.getNumero());

        if (agencia != null) {
            throw new NotUniqueException(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null));
        }

        agencia = new Agencia();
        agencia.setEndereco(new Endereco());

        Agencia agenciaToSave = dtoToEntity(agenciaDTO, agencia, banco, cidade);

        return agenciaRepository.save(agenciaToSave);
    }

    public Agencia update(long id, AgenciaDTO agenciaDTO) throws NotFoundException, NotUniqueException {
        Banco banco = bancoService.findById(agenciaDTO.getBancoId());
        Cidade cidade = cidadeService.findById(agenciaDTO.getEndereco().getCidadeId());

        Agencia agencia = agenciaRepository.findByNumeroAndDifferentId(agenciaDTO.getNumero(), id);

        if (agencia != null && !agencia.getId().equals(id)) {
            throw new NotUniqueException(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null));
        }

        agencia = findById(id);

        Agencia agenciaToUpdate = dtoToEntity(agenciaDTO, agencia, banco, cidade);

        return agenciaRepository.save(agenciaToUpdate);
    }

    private Agencia dtoToEntity(AgenciaDTO agenciaDTO, Agencia agencia, Banco banco, Cidade cidade) {
        agencia.setNome(agenciaDTO.getNome());
        agencia.setNumero(agenciaDTO.getNumero());
        agencia.setBanco(banco);
        agencia.getEndereco().setLogradouro(agenciaDTO.getEndereco().getLogradouro());
        agencia.getEndereco().setNumero(agenciaDTO.getEndereco().getNumero());
        agencia.getEndereco().setComplemento(agenciaDTO.getEndereco().getComplemento());
        agencia.getEndereco().setBairro(agenciaDTO.getEndereco().getBairro());
        agencia.getEndereco().setCep(agenciaDTO.getEndereco().getCep());
        agencia.getEndereco().setCidade(cidade);

        return agencia;
    }
}
