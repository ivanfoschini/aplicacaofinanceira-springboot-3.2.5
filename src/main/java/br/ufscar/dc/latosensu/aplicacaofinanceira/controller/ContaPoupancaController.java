package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ContaPoupanca;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.ContaPoupancaService;
import java.util.List;
import javax.validation.Valid;
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
@RequestMapping(value = "/contaPoupanca")
public class ContaPoupancaController {

    @Autowired
    private ContaPoupancaService contaPoupancaService;

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) throws NotEmptyCollectionException, NotFoundException {
        contaPoupancaService.delete(id);
    }

    @GetMapping("/list")
    public List<ContaPoupanca> findAll() {
        return contaPoupancaService.findAll();
    }

    @GetMapping("/show/{id}")
    public ContaPoupanca findById(@PathVariable("id") long id) throws NotFoundException {
        return contaPoupancaService.findById(id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ContaPoupanca save(@RequestBody @Valid ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException {
        return contaPoupancaService.save(contaPoupanca);
    }

    @PutMapping("/update/{id}")
    public ContaPoupanca update(@PathVariable("id") long id, @RequestBody @Valid ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException {
        return contaPoupancaService.update(id, contaPoupanca);
    }
}
