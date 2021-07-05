package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ForbiddenException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.UnauthorizedException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Papel;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Servico;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Usuario;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ServicoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.UsuarioRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.security.SecurityUtil;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    public boolean autorizar(String requestUri, String token) throws ForbiddenException {
        String nomeDeUsuario = new SecurityUtil().getNomeDeUsuario(token);
        Usuario usuario = usuarioRepository.findByNomeDeUsuario(nomeDeUsuario);
        Servico servico = servicoRepository.findByUri(requestUri);
        
        if (usuario != null && servico != null) {
            for (Papel papelDoUsuario: usuario.getPapeis()) {
                for (Papel papelDoServico: servico.getPapeis()) {
                    if (papelDoUsuario.getNome().equals(papelDoServico.getNome())) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public String login(String nomeDeUsuario, String senha) throws UnauthorizedException, NoSuchAlgorithmException {
        SecurityUtil securityUtil = new SecurityUtil();
        
        if (nomeDeUsuario == null || senha == null) {
            throw new UnauthorizedException();
        }
        
        Usuario usuario = usuarioRepository.findByNomeDeUsuarioAndSenha(nomeDeUsuario, securityUtil.generateMD5(senha));

        if (usuario == null) {
            throw new UnauthorizedException();
        }        
        
        return securityUtil.getToken(usuario.getNomeDeUsuario());
    }
}
