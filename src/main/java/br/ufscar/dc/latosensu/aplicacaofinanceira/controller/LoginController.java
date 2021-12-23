package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.LoginDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.TokenDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.UnauthorizedException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.SecurityService;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class LoginController {
    
    @Autowired
    private SecurityService securityService;
    
    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO loginDTO) throws UnauthorizedException, NoSuchAlgorithmException {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(securityService.login(loginDTO.getNomeDeUsuario(), loginDTO.getSenha()));
        
        return tokenDTO;
    }
}
