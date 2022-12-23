package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.CorrentistaDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.*;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Correntista;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.CorrentistaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/correntista")
public class CorrentistaController {
    
    @Autowired
    private CorrentistaService correntistaService;
    
    @PostMapping("/associate")
    public List<Correntista> associate(@RequestBody @Valid CorrentistaDTO correntistaDTO) throws HttpMessageNotReadableException, MoreThanOneAccountClientException, MoreThanOneAccountOwnershipException, NoAccountOwnershipException, NotFoundException {
        return correntistaService.associate(correntistaDTO);
    }
    
    @GetMapping("/listByCliente/{id}")
    public List<Correntista> listByCliente(@PathVariable("id") long id) {
        return correntistaService.listByCliente(id);
    }
    
    @GetMapping("/listByConta/{id}")
    public List<Correntista> listByConta(@PathVariable("id") long id) {
        return correntistaService.listByConta(id);
    }
}
