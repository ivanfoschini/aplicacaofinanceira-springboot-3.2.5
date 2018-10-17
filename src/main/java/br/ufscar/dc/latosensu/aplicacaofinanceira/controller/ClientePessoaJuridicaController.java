package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ValidationException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaJuridica;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.ClientePessoaJuridicaService;
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
@RequestMapping(value = "/clientePessoaJuridica")
public class ClientePessoaJuridicaController extends BaseController {

    @Autowired
    private ClientePessoaJuridicaService clientePessoaJuridicaService;

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") long id) throws NotEmptyCollectionException, NotFoundException {
        clientePessoaJuridicaService.delete(id);
    }

    @GetMapping("/list")
    public List<ClientePessoaJuridica> findAll() {
        return clientePessoaJuridicaService.findAll();
    }

    @GetMapping("/show/{id}")
    public ClientePessoaJuridica findById(@PathVariable("id") long id) throws NotFoundException {
        return clientePessoaJuridicaService.findById(id);
    }

    @PostMapping("/save")
    public ClientePessoaJuridica save(@RequestBody @Valid ClientePessoaJuridica clientePessoaJuridica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException {
        return clientePessoaJuridicaService.save(clientePessoaJuridica, bindingResult);
    }

    @PutMapping("/update/{id}")
    public ClientePessoaJuridica update(@PathVariable("id") long id, @RequestBody @Valid ClientePessoaJuridica clientePessoaJuridica, BindingResult bindingResult) throws EmptyCollectionException, NotFoundException, NotUniqueException, ValidationException {
        return clientePessoaJuridicaService.update(id, clientePessoaJuridica, bindingResult);
    }
}
