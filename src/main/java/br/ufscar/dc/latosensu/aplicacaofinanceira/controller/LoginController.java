package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.LoginDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.TokenDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.UnauthorizedException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.SecurityService;
import br.ufscar.dc.latosensu.aplicacaofinanceira.swagger.LoginSwagger.AplicacaoFinanceiraLoginSwagger;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
@Tag(name = "Login")
@ApiResponse(responseCode = "401", description = "Usuário não autorizado a acessar a aplicação",
        content = @Content(mediaType = "application/json"))
public class LoginController {

    @Autowired
    private SecurityService securityService;

    @PostMapping("/login")
    @AplicacaoFinanceiraLoginSwagger
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) throws UnauthorizedException {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(securityService.login(loginDTO.getNomeDeUsuario(), loginDTO.getSenha()));

        return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
    }
}