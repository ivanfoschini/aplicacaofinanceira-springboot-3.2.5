package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaJuridica;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.ClientePessoaJuridicaService;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class ClientePessoaJuridicaController {

    @Autowired
    private ClientePessoaJuridicaService clientePessoaJuridicaService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) throws NotEmptyCollectionException, NotFoundException {
        clientePessoaJuridicaService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClientePessoaJuridica>> findAll() {
        List<ClientePessoaJuridica> clientesPessoasJuridicas = clientePessoaJuridicaService.findAll();

        return new ResponseEntity<>(clientesPessoasJuridicas, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ClientePessoaJuridica> findById(@PathVariable("id") long id) throws NotFoundException {
        ClientePessoaJuridica clientePessoaJuridica = clientePessoaJuridicaService.findById(id);

        return new ResponseEntity<>(clientePessoaJuridica, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ClientePessoaJuridica> save(@RequestBody @Valid ClientePessoaJuridica clientePessoaJuridica) throws EmptyCollectionException, NotFoundException, NotUniqueException {
        ClientePessoaJuridica savedClientePessoaJuridica = clientePessoaJuridicaService.save(clientePessoaJuridica);

        return new ResponseEntity<>(savedClientePessoaJuridica, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClientePessoaJuridica> update(@PathVariable("id") long id, @RequestBody @Valid ClientePessoaJuridica clientePessoaJuridica) throws EmptyCollectionException, NotFoundException, NotUniqueException {
        ClientePessoaJuridica updatedClientePessoaJuridica = clientePessoaJuridicaService.update(id, clientePessoaJuridica);

        return new ResponseEntity<>(updatedClientePessoaJuridica, HttpStatus.OK);
    }
}