package br.ufscar.dc.latosensu.aplicacaofinanceira.swagger;

import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.LoginDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface LoginSwagger {

    @Operation(summary = "Efetua o login, a partir das credenciais do usuário")
    @RequestBody(content = { @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{ \"nomeDeUsuario\": \"funcionario\", \"senha\": \"funcionario\" }"),
            schema = @Schema(implementation = LoginDTO.class)) })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token de acesso",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"token\": \"eyJhbGciOiJIUzI1NiJ9...\" }"),
                            schema = @Schema(implementation = TokenDTO.class)) })
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface AplicacaoFinanceiraLoginSwagger {}
}
