package br.ufscar.dc.latosensu.aplicacaofinanceira.interceptor;

import br.ufscar.dc.latosensu.aplicacaofinanceira.service.UsuarioService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AutorizacaoInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UsuarioService usuarioService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {  
        String[] requestUri = request.getRequestURI().split("/");        
        String token = request.getHeader("token");
        
        if (requestUri.length < 3 || requestUri[1] == null || requestUri[2] == null || token == null ||
            !usuarioService.autorizar("/" + requestUri[1] + "/" + requestUri[2], token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        } 
          
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model) throws Exception {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3) throws Exception {}
}
