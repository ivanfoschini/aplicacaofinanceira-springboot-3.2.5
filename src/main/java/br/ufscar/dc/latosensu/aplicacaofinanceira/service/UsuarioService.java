package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Usuario;
import java.security.NoSuchAlgorithmException;

public interface UsuarioService {
    
    boolean autorizar(String requestUri, String token);
    
    String login(String nomeDeUsuario, String senha) throws NotFoundException, NoSuchAlgorithmException;
}
