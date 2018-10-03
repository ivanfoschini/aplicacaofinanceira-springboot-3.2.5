package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.AgenciaService;
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
@RequestMapping(value = "/agencia")
public class AgenciaController extends BaseController {

    @Autowired
    private AgenciaService agenciaService;

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") long id) throws NotFoundException {
        agenciaService.delete(id);
    }

    @GetMapping("/list")
    public List<Agencia> findAll() {
        return agenciaService.findAll();
    }

    @GetMapping("/show/{id}")
    public Agencia findById(@PathVariable("id") long id) throws NotFoundException {
        return agenciaService.findById(id);
    }

    @PostMapping("/save")
    public Agencia save(@RequestBody @Valid Agencia agencia, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        return agenciaService.save(agencia, bindingResult);
    }

    @PutMapping("/update/{id}")
    public Agencia update(@PathVariable("id") long id, @RequestBody @Valid Agencia agencia, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        return agenciaService.update(id, agencia, bindingResult);
    }
}
