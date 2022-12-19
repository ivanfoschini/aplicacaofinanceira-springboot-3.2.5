package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Estado;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.EstadoService;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value = "/estado")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) throws NotFoundException {
        estadoService.delete(id);
    }

    @GetMapping("/list")
    public List<Estado> findAll() {
        return estadoService.findAll();
    }

    @GetMapping("/show/{id}")
    public Estado findById(@PathVariable("id") long id) throws NotFoundException {
        return estadoService.findById(id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Estado save(@RequestBody @Valid Estado estado) throws NotUniqueException {
        return estadoService.save(estado);
    }

    @PutMapping("/update/{id}")
    public Estado update(@PathVariable("id") long id, @RequestBody @Valid Estado estado) throws NotFoundException, NotUniqueException {
        return estadoService.update(id, estado);
    }
}
