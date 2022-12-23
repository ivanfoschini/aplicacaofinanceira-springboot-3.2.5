package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaJuridica;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.ClientePessoaJuridicaService;
import io.swagger.v3.oas.annotations.Hidden;
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
@RequestMapping(value = "/clientePessoaJuridica")
@Hidden
public class ClientePessoaJuridicaController {

    @Autowired
    private ClientePessoaJuridicaService clientePessoaJuridicaService;

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
    @ResponseStatus(HttpStatus.CREATED)
    public ClientePessoaJuridica save(@RequestBody @Valid ClientePessoaJuridica clientePessoaJuridica) throws EmptyCollectionException, NotFoundException {
        return clientePessoaJuridicaService.save(clientePessoaJuridica);
    }

    @PutMapping("/update/{id}")
    public ClientePessoaJuridica update(@PathVariable("id") long id, @RequestBody @Valid ClientePessoaJuridica clientePessoaJuridica) throws EmptyCollectionException, NotFoundException {
        return clientePessoaJuridicaService.update(id, clientePessoaJuridica);
    }
}
