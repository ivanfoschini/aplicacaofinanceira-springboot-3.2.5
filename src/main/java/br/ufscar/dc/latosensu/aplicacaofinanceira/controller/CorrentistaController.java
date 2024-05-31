package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.CorrentistaDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.*;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Correntista;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.CorrentistaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Correntista>> associate(@RequestBody @Valid CorrentistaDTO correntistaDTO) throws HttpMessageNotReadableException, MoreThanOneAccountClientException, MoreThanOneAccountOwnershipException, NoAccountOwnershipException, NotFoundException {
        List<Correntista> correntistas = correntistaService.associate(correntistaDTO);

        return new ResponseEntity<>(correntistas, HttpStatus.OK);
    }
    
    @GetMapping("/listByCliente/{id}")
    public ResponseEntity<List<Correntista>> listByCliente(@PathVariable("id") long id) {
        List<Correntista> correntistas = correntistaService.listByCliente(id);

        return new ResponseEntity<>(correntistas, HttpStatus.OK);
    }
    
    @GetMapping("/listByConta/{id}")
    public ResponseEntity<List<Correntista>> listByConta(@PathVariable("id") long id) {
        List<Correntista> correntistas = correntistaService.listByConta(id);

        return new ResponseEntity<>(correntistas, HttpStatus.OK);
    }
}
