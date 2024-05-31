package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.EmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaFisica;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.ClientePessoaFisicaService;
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
@RequestMapping(value = "/clientePessoaFisica")
public class ClientePessoaFisicaController {

    @Autowired
    private ClientePessoaFisicaService clientePessoaFisicaService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) throws NotFoundException {
        clientePessoaFisicaService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClientePessoaFisica>> findAll() {
        List<ClientePessoaFisica> clientesPessoasFisicas = clientePessoaFisicaService.findAll();

        return new ResponseEntity<>(clientesPessoasFisicas, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ClientePessoaFisica> findById(@PathVariable("id") long id) throws NotFoundException {
        ClientePessoaFisica clientePessoaFisica = clientePessoaFisicaService.findById(id);

        return new ResponseEntity<>(clientePessoaFisica, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ClientePessoaFisica> save(@RequestBody @Valid ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException, NotFoundException, NotUniqueException {
        ClientePessoaFisica savedClientePessoaFisica = clientePessoaFisicaService.save(clientePessoaFisica);

        return new ResponseEntity<>(savedClientePessoaFisica, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClientePessoaFisica> update(@PathVariable("id") long id, @RequestBody @Valid ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException, NotFoundException, NotUniqueException {
        ClientePessoaFisica updatedClientePessoaFisica = clientePessoaFisicaService.update(id, clientePessoaFisica);

        return new ResponseEntity<>(updatedClientePessoaFisica, HttpStatus.OK);
    }
}
