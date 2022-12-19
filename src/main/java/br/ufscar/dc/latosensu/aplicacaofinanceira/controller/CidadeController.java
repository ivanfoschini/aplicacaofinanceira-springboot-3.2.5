package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.CidadeService;
import java.util.List;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/cidade")
public class CidadeController {

    @Autowired
    private CidadeService cidadeService;

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) throws NotFoundException {
        cidadeService.delete(id);
    }

    @GetMapping("/list")
    public List<Cidade> findAll() {
        return cidadeService.findAll();
    }

    @GetMapping("/show/{id}")
    public Cidade findById(@PathVariable("id") long id) throws NotFoundException {
        return cidadeService.findById(id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Cidade save(@RequestBody @Valid Cidade cidade) throws NotFoundException, NotUniqueException {
        return cidadeService.save(cidade);
    }

    @PutMapping("/update/{id}")
    public Cidade update(@PathVariable("id") long id, @RequestBody @Valid Cidade cidade) throws NotFoundException, NotUniqueException {
        return cidadeService.update(id, cidade);
    }
}
