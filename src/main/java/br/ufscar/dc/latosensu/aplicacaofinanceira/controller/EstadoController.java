package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Estado;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.EstadoService;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value = "/estado")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) throws NotFoundException, NotEmptyCollectionException {
        estadoService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Estado>> findAll() {
        List<Estado> estados = estadoService.findAll();

        return new ResponseEntity<>(estados, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Estado> findById(@PathVariable("id") long id) throws NotFoundException {
        Estado estado = estadoService.findById(id);

        return new ResponseEntity<>(estado, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Estado> save(@RequestBody @Valid Estado estado) throws NotUniqueException {
        Estado savedEstado = estadoService.save(estado);

        return new ResponseEntity<>(savedEstado, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Estado> update(@PathVariable("id") long id, @RequestBody @Valid Estado estado) throws NotFoundException, NotUniqueException {
        Estado updatedEstado = estadoService.update(id, estado);

        return new ResponseEntity<>(updatedEstado, HttpStatus.OK);
    }
}
