package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotEmptyCollectionException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotUniqueException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.BancoService;
import br.ufscar.dc.latosensu.aplicacaofinanceira.swagger.BancoSwagger.DeleteSwagger;
import br.ufscar.dc.latosensu.aplicacaofinanceira.swagger.BancoSwagger.FindAllSwagger;
import br.ufscar.dc.latosensu.aplicacaofinanceira.swagger.BancoSwagger.FindByIdSwagger;
import br.ufscar.dc.latosensu.aplicacaofinanceira.swagger.BancoSwagger.SaveSwagger;
import br.ufscar.dc.latosensu.aplicacaofinanceira.swagger.BancoSwagger.UpdateSwagger;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping(value = "/banco")
@Tag(name = "Banco")
@ApiResponse(responseCode = "401", description = "Usuário não autorizado a acessar a aplicação",
        content = @Content(mediaType = "application/json"))
@ApiResponse(responseCode = "403", description = "Papel não autorizado a acessar o recurso",
        content = @Content(mediaType = "application/json"))
public class BancoController {

    @Autowired
    private BancoService bancoService;

    @DeleteMapping("/delete/{id}")
    @DeleteSwagger
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) throws NotEmptyCollectionException, NotFoundException {
        bancoService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    @FindAllSwagger
    public ResponseEntity<List<Banco>> findAll() {
        List<Banco> bancos = bancoService.findAll();

        return new ResponseEntity<>(bancos, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    @FindByIdSwagger
    public ResponseEntity<Banco> findById(@PathVariable("id") long id) throws NotFoundException {
        Banco banco = bancoService.findById(id);

        return new ResponseEntity<>(banco, HttpStatus.OK);
    }

    @PostMapping("/save")
    @SaveSwagger
    public ResponseEntity<Banco> save(@RequestBody @Valid Banco banco) throws NotUniqueException {
        Banco savedBanco = bancoService.save(banco);

        return new ResponseEntity<>(savedBanco, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @UpdateSwagger
    public ResponseEntity<Banco> update(@PathVariable("id") long id, @RequestBody @Valid Banco banco) throws NotFoundException, NotUniqueException {
        Banco updatedBanco = bancoService.update(id, banco);

        return new ResponseEntity<>(updatedBanco, HttpStatus.OK);
    }
}