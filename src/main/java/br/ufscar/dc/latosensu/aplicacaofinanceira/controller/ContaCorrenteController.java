package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaCorrente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.ContaCorrenteService;
import java.util.List;
import javax.validation.Valid;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/contaCorrente")
@Hidden
public class ContaCorrenteController {

    @Autowired
    private ContaCorrenteService contaCorrenteService;

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) throws NotEmptyCollectionException, NotFoundException {
        contaCorrenteService.delete(id);
    }

    @GetMapping("/list")
    public List<ContaCorrente> findAll() {
        return contaCorrenteService.findAll();
    }

    @GetMapping("/show/{id}")
    public ContaCorrente findById(@PathVariable("id") long id) throws NotFoundException {
        return contaCorrenteService.findById(id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ContaCorrente save(@RequestBody @Valid ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException {
        return contaCorrenteService.save(contaCorrente);
    }

    @PutMapping("/update/{id}")
    public ContaCorrente update(@PathVariable("id") long id, @RequestBody @Valid ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException {
        return contaCorrenteService.update(id, contaCorrente);
    }
}
