package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.CorrentistaDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.DifferentAccountsException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.MoreThanOneAccountClientException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.MoreThanOneAccountOwnershipException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NoAccountOwnershipException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Correntista;
import java.util.List;
import org.springframework.http.converter.HttpMessageNotReadableException;

public interface CorrentistaService {

    List<Correntista> associate(List<CorrentistaDTO> correntistasDTO) throws DifferentAccountsException, HttpMessageNotReadableException, MoreThanOneAccountClientException, MoreThanOneAccountOwnershipException, NoAccountOwnershipException, NotFoundException;    
    
    List<Correntista> listByCliente(Long clienteId);

    List<Correntista> listByConta(Long contaId);
}
