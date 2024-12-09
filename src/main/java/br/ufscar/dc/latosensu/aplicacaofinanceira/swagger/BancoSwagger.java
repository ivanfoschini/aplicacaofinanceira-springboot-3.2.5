package br.ufscar.dc.latosensu.aplicacaofinanceira.swagger;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

public interface BancoSwagger {

    // DeleteSwagger
    @Operation(summary = "Exclui um banco",
            parameters = { @Parameter(name = "token", description = "Token JWT de acesso obtido no login",
                    in = ParameterIn.HEADER, schema = @Schema(implementation = String.class), required = true),
                    @Parameter(name = "id", description = "Identificador do banco",
                            in = ParameterIn.PATH, required = true)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Banco excluído com sucesso",
                    content = @Content(mediaType = "application/json"))
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface DeleteSwagger {}

    // FindAllSwagger
    @Operation(summary = "Retorna uma listagem de bancos, ordenados pelo nome do banco",
            parameters = { @Parameter(name = "token", description = "Token JWT de acesso obtido no login",
                    in = ParameterIn.HEADER, schema = @Schema(implementation = String.class), required = true)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem dos bancos encontrados",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "[{ \"id\": 1, \"numero\": 10, \"cnpj\": \"00000000000191\", \"nome\": \"Banco do Brasil\" }, { \"id\": 2, \"numero\": 20, \"cnpj\": \"00360305000104\", \"nome\": \"Caixa Econômica Federal\" }]"),
                            schema = @Schema(implementation = Banco.class)) })
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FindAllSwagger {}

    // FindByIdSwagger
    @Operation(summary = "Retorna um único banco, a partir do identificador do banco",
            parameters = { @Parameter(name = "token", description = "Token JWT de acesso obtido no login",
                    in = ParameterIn.HEADER, schema = @Schema(implementation = String.class), required = true),
                    @Parameter(name = "id", description = "Identificador do banco",
                            in = ParameterIn.PATH, required = true)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações do banco encontrado",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"id\": 1, \"numero\": 10, \"cnpj\": \"00000000000191\", \"nome\": \"Banco do Brasil\" }"),
                            schema = @Schema(implementation = Banco.class)) }),
            @ApiResponse(responseCode = "404", description = "Banco não encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FindByIdSwagger {}

    // SaveSwagger
    @Operation(summary = "Insere um novo banco",
            parameters = { @Parameter(name = "token", description = "Token JWT de acesso obtido no login",
                    in = ParameterIn.HEADER, schema = @Schema(implementation = String.class), required = true)})
    @RequestBody(content = { @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{ \"numero\": 10, \"cnpj\": \"00000000000191\", \"nome\": \"Banco do Brasil\" }"),
            schema = @Schema(implementation = Banco.class)) })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Banco inserido com sucesso",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"id\": 1, \"numero\": 10, \"cnpj\": \"00000000000191\", \"nome\": \"Banco do Brasil\" }"),
                            schema = @Schema(implementation = Banco.class)) }),
            @ApiResponse(responseCode = "422", description = "Não foi possível processar a requisição",
                    content = @Content(mediaType = "application/json"))
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface SaveSwagger {}

    // UpdateSwagger
    @Operation(summary = "Altera os dados de um banco",
            parameters = { @Parameter(name = "token", description = "Token JWT de acesso obtido no login",
                    in = ParameterIn.HEADER, schema = @Schema(implementation = String.class), required = true),
                    @Parameter(name = "id", description = "Identificador do banco",
                            in = ParameterIn.PATH, required = true)})
    @RequestBody(content = { @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{ \"numero\": 10, \"cnpj\": \"00000000000191\", \"nome\": \"Banco do Brasil\" }"),
            schema = @Schema(implementation = Banco.class)) })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Banco alterado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"id\": 1, \"numero\": 10, \"cnpj\": \"00000000000191\", \"nome\": \"Banco do Brasil\" }"),
                            schema = @Schema(implementation = Banco.class)) }),
            @ApiResponse(responseCode = "404", description = "Banco não encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Não foi possível processar a requisição",
                    content = @Content(mediaType = "application/json"))
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface UpdateSwagger {}
}
