package br.ufscar.dc.latosensu.aplicacaofinanceira;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.schema.ModelRef;

@Configuration
@EnableSwagger2
public class AplicacaoFinanceiraApplicationSwaggerConfig {
        
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("br.ufscar.dc.latosensu.aplicacaofinanceira"))
            .paths(regex("/banco.*|/estado.*")) //.paths(regex("/.*")) para todos os controllers, com exceção dos que estiverem com a anotação @ApiIgnore
            .build()
            .apiInfo(apiInfo())
            .globalOperationParameters(
                Arrays.asList(new ParameterBuilder()
                .name("token")
                .description("Token obtido no processo de login")
                .parameterType("header")        
                .modelRef(new ModelRef("string"))                
                .build())
            );
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
            "Aplicação Financeira - Documentação dos serviços web",
            "Documentação dos serviços web da disciplina <b>Desenvolvimento de Software em Java</b> do Curso de Pós-Graduação Lato Sensu - Desenvolvimento de Software para Web.",
            "1.0",
            null,
            null,
            null,
            null, 
            new ArrayList<VendorExtension>()
        );

        return apiInfo;
    }
}
