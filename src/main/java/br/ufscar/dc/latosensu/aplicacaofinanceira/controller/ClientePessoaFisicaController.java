package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaFisica;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.ClientePessoaFisicaService;
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
@RequestMapping(value = "/clientePessoaFisica")
@Hidden
public class ClientePessoaFisicaController {

    @Autowired
    private ClientePessoaFisicaService clientePessoaFisicaService;

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
    @ResponseStatus(HttpStatus.CREATED)
    public ClientePessoaFisica save(@RequestBody @Valid ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException, NotFoundException {
        return clientePessoaFisicaService.save(clientePessoaFisica);
    }

    @PutMapping("/update/{id}")
    public ClientePessoaFisica update(@PathVariable("id") long id, @RequestBody @Valid ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException, NotFoundException {
        return clientePessoaFisicaService.update(id, clientePessoaFisica);
    }
}
