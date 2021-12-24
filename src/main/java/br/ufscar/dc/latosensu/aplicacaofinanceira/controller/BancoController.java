package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.BancoService;
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
@RequestMapping(value = "/banco")
public class BancoController {

    @Autowired
    private BancoService bancoService;

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) throws NotEmptyCollectionException, NotFoundException {
        bancoService.delete(id);
    }

    @GetMapping("/list")
    public List<Banco> findAll() {
        return bancoService.findAll();
    }

    @GetMapping("/show/{id}")
    public Banco findById(@PathVariable("id") long id) throws NotFoundException {
        return bancoService.findById(id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Banco save(@RequestBody @Valid Banco banco) throws NotUniqueException {
        return bancoService.save(banco);
    }

    @PutMapping("/update/{id}")
    public Banco update(@PathVariable("id") long id, @RequestBody @Valid Banco banco) throws NotFoundException, NotUniqueException {
        return bancoService.update(id, banco);
    }
}
