package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Estado;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.EstadoService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/estado")
public class EstadoController extends BaseController {

    @Autowired
    private EstadoService estadoService;

    @DeleteMapping("/delete/{id}")
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
    public Estado save(@RequestBody @Valid Estado estado, BindingResult bindingResult) throws NotUniqueException, ValidationException {
        return estadoService.save(estado, bindingResult);
    }

    @PutMapping("/update/{id}")
    public Estado update(@PathVariable("id") long id, @RequestBody @Valid Estado estado, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        return estadoService.update(id, estado, bindingResult);
    }
}
