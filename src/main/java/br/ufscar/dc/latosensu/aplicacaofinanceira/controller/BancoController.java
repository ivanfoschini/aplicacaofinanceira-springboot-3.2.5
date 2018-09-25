package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.BancoService;
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
@RequestMapping(value="/banco")
public class BancoController extends BaseController {

    @Autowired
    private BancoService bancoService;

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") long id) throws NotFoundException {
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
    public Banco save(@RequestBody @Valid Banco banco, BindingResult bindingResult) throws NotUniqueException, ValidationException {
        return bancoService.save(banco, bindingResult);
    }

    @PutMapping("/update/{id}")
    public Banco update(@PathVariable("id") long id, @RequestBody @Valid Banco banco, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        return bancoService.update(id, banco, bindingResult);
    }
}
