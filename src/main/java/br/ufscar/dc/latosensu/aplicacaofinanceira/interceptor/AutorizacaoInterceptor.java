package br.ufscar.dc.latosensu.aplicacaofinanceira.interceptor;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ForbiddenException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.UsuarioService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AutorizacaoInterceptor implements HandlerInterceptor {

    @Autowired
    private UsuarioService usuarioService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        String[] requestUri = request.getRequestURI().split("/");        
        String token = request.getHeader("token");

        if (requestUri.length < 3 || requestUri[1] == null || requestUri[2] == null ||
                token == null || token.trim() == "" ||
                !usuarioService.autorizar("/" + requestUri[1] + "/" + requestUri[2], token)) {
            throw new ForbiddenException();
        } 
          
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model) throws Exception {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3) throws Exception {}
}
