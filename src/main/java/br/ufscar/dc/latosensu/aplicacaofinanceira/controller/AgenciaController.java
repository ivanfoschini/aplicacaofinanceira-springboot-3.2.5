package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Agencia;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.AgenciaService;
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
@RequestMapping(value = "/agencia")
public class AgenciaController {

    @Autowired
    private AgenciaService agenciaService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) throws NotFoundException {
        agenciaService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Agencia>> findAll() {
        List<Agencia> agencias = agenciaService.findAll();

        return new ResponseEntity<>(agencias, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Agencia> findById(@PathVariable("id") long id) throws NotFoundException {
        Agencia agencia = agenciaService.findById(id);

        return new ResponseEntity<>(agencia, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Agencia> save(@RequestBody @Valid Agencia agencia) throws NotFoundException, NotUniqueException {
        Agencia savedAgencia = agenciaService.save(agencia);

        return new ResponseEntity<>(savedAgencia, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Agencia> update(@PathVariable("id") long id, @RequestBody @Valid Agencia agencia) throws NotFoundException, NotUniqueException {
        Agencia updatedAgencia = agenciaService.update(id, agencia);

        return new ResponseEntity<>(updatedAgencia, HttpStatus.OK);
    }
}
