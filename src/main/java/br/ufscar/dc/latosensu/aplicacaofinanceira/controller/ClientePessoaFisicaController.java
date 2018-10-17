package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaFisica;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.ClientePessoaFisicaService;
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
@RequestMapping(value = "/clientePessoaFisica")
public class ClientePessoaFisicaController extends BaseController {

    @Autowired
    private ClientePessoaFisicaService clientePessoaFisicaService;

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") long id) throws NotEmptyCollectionException, NotFoundException {
        clientePessoaFisicaService.delete(id);
    }

    @GetMapping("/list")
    public List<ClientePessoaFisica> findAll() {
        return clientePessoaFisicaService.findAll();
    }

    @GetMapping("/show/{id}")
    public ClientePessoaFisica findById(@PathVariable("id") long id) throws NotFoundException {
        return clientePessoaFisicaService.findById(id);
    }

    @PostMapping("/save")
    public ClientePessoaFisica save(@RequestBody @Valid ClientePessoaFisica clientePessoaFisica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException {
        return clientePessoaFisicaService.save(clientePessoaFisica, bindingResult);
    }

    @PutMapping("/update/{id}")
    public ClientePessoaFisica update(@PathVariable("id") long id, @RequestBody @Valid ClientePessoaFisica clientePessoaFisica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException {
        return clientePessoaFisicaService.update(id, clientePessoaFisica, bindingResult);
    }
}
