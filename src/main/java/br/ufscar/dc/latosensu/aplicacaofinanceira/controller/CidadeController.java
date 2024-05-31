package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.CidadeService;
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
@RequestMapping(value = "/cidade")
public class CidadeController {

    @Autowired
    private CidadeService cidadeService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) throws NotEmptyCollectionException, NotFoundException {
        cidadeService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Cidade>> findAll() {
        List<Cidade> cidades = cidadeService.findAll();

        return new ResponseEntity<>(cidades, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Cidade> findById(@PathVariable("id") long id) throws NotFoundException {
        Cidade cidade = cidadeService.findById(id);

        return new ResponseEntity<>(cidade, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Cidade> save(@RequestBody @Valid Cidade cidade) throws NotFoundException, NotUniqueException {
        Cidade savedCidade = cidadeService.save(cidade);

        return new ResponseEntity<>(savedCidade, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Cidade> update(@PathVariable("id") long id, @RequestBody @Valid Cidade cidade) throws NotFoundException, NotUniqueException {
        Cidade updatedCidade = cidadeService.update(id, cidade);

        return new ResponseEntity<>(updatedCidade, HttpStatus.OK);
    }
}
