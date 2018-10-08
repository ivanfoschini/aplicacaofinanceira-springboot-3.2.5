package br.ufscar.dc.latosensu.aplicacaofinanceira;

import br.ufscar.dc.latosensu.aplicacaofinanceira.interceptor.AutorizacaoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AplicacaoFinanceiraApplicationConfig extends WebMvcConfigurerAdapter {

    @Autowired
    AutorizacaoInterceptor autorizacaoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(autorizacaoInterceptor).excludePathPatterns("/login").pathMatcher(new AntPathMatcher());
    }    
}
