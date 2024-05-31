package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaPoupanca;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.ContaPoupancaService;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/contaPoupanca")
public class ContaPoupancaController {

    @Autowired
    private ContaPoupancaService contaPoupancaService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) throws NotEmptyCollectionException, NotFoundException {
        contaPoupancaService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ContaPoupanca>> findAll() {
        List<ContaPoupanca> contasPoupancas = contaPoupancaService.findAll();

        return new ResponseEntity<>(contasPoupancas, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ContaPoupanca> findById(@PathVariable("id") long id) throws NotFoundException {
        ContaPoupanca contaPoupanca = contaPoupancaService.findById(id);

        return new ResponseEntity<>(contaPoupanca, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ContaPoupanca> save(@RequestBody @Valid ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException {
        ContaPoupanca savedContaPoupanca = contaPoupancaService.save(contaPoupanca);

        return new ResponseEntity<>(savedContaPoupanca, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ContaPoupanca> update(@PathVariable("id") long id, @RequestBody @Valid ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException {
        ContaPoupanca updatedContaPoupanca = contaPoupancaService.update(id, contaPoupanca);

        return new ResponseEntity<>(updatedContaPoupanca, HttpStatus.OK);
    }
}