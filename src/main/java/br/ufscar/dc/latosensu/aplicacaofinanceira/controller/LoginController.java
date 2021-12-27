package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.LoginDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.TokenDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.UnauthorizedException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Operation(summary = "Efetua o login, a partir das credenciais do usuário")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = { @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{ \"nomeDeUsuario\": \"funcionario\", \"senha\": \"funcionario\" }"),
            schema = @Schema(implementation = Banco.class)) })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token de acesso",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"token\": \"eyJhbGciOiJIUzI1NiJ9...\" }"),
                            schema = @Schema(implementation = LoginDTO.class)) })
    })
    public TokenDTO login(@RequestBody LoginDTO loginDTO) throws UnauthorizedException {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(securityService.login(loginDTO.getNomeDeUsuario(), loginDTO.getSenha()));
        
        return tokenDTO;
    }
}
