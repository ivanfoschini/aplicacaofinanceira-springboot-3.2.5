package br.ufscar.dc.latosensu.aplicacaofinanceira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class AplicacaoFinanceiraApplication {

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("ValidationMessages", "i18n/messages");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

    public static void main(String[] args) {
        SpringApplication.run(AplicacaoFinanceiraApplication.class, args);
    }
}
