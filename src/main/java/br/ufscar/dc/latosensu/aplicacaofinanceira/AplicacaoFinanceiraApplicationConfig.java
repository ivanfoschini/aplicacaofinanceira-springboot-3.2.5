package br.ufscar.dc.latosensu.aplicacaofinanceira;

import br.ufscar.dc.latosensu.aplicacaofinanceira.interceptor.AutorizacaoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AplicacaoFinanceiraApplicationConfig implements WebMvcConfigurer {

    @Autowired
    AutorizacaoInterceptor autorizacaoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(autorizacaoInterceptor)
                .addPathPatterns("/agencia/**")
                .addPathPatterns("/banco/**")
                .addPathPatterns("/cidade/**")
                .addPathPatterns("/clientePessoaFisica/**")
                .addPathPatterns("/clientePessoaJuridica/**")
                .addPathPatterns("/contaCorrente/**")
                .addPathPatterns("/contaPoupanca/**")
                .addPathPatterns("/correntista/**")
                .addPathPatterns("/estado/**")
                .pathMatcher(new AntPathMatcher());
    }    
}
