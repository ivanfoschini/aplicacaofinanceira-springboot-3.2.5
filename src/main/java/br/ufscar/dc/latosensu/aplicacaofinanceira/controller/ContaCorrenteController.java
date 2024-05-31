package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaCorrente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.ContaCorrenteService;
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
@RequestMapping(value = "/contaCorrente")
public class ContaCorrenteController {

    @Autowired
    private ContaCorrenteService contaCorrenteService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) throws NotFoundException {
        contaCorrenteService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ContaCorrente>> findAll() {
        List<ContaCorrente> contasCorrentes = contaCorrenteService.findAll();

        return new ResponseEntity<>(contasCorrentes, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ContaCorrente> findById(@PathVariable("id") long id) throws NotFoundException {
        ContaCorrente contaCorrente = contaCorrenteService.findById(id);

        return new ResponseEntity<>(contaCorrente, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ContaCorrente> save(@RequestBody @Valid ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException {
        ContaCorrente savedContaCorrente = contaCorrenteService.save(contaCorrente);

        return new ResponseEntity<>(savedContaCorrente, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ContaCorrente> update(@PathVariable("id") long id, @RequestBody @Valid ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException {
        ContaCorrente updatedContaCorrente = contaCorrenteService.update(id, contaCorrente);

        return new ResponseEntity<>(updatedContaCorrente, HttpStatus.OK);
    }
}
