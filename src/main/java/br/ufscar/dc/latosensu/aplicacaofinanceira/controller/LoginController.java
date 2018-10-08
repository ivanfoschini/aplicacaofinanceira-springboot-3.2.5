package br.ufscar.dc.latosensu.aplicacaofinanceira.controller;

import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.LoginDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject.TokenDTO;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Usuario;
import br.ufscar.dc.latosensu.aplicacaofinanceira.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class LoginController extends BaseController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO loginDTO) throws JsonProcessingException, NotFoundException, NoSuchAlgorithmException {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(usuarioService.login(loginDTO.getNomeDeUsuario(), loginDTO.getSenha()));
        
        return tokenDTO;
    }
}
