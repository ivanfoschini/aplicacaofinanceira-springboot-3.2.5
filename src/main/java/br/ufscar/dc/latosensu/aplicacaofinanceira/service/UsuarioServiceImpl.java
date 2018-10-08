package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.NotFoundException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Papel;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Servico;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Usuario;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ServicoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {   
    
    private static final String JWT_TOKEN_KEY = "LATOSENSU";
    private static final Long JWT_TOKEN_EXPIRATION_TIME = 86400000L; //Um dia em milisegundos      

    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Override
    public boolean autorizar(String requestUri, String token) {
        String nomeDeUsuario = getNomeDeUsuario(token);
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
    
    @Override
    public String login(String nomeDeUsuario, String senha) throws NotFoundException, NoSuchAlgorithmException {
        if (senha == null) {
            throw new NotFoundException(messageSource.getMessage("usuarioNaoEncontrado", null, null));
        }
        
        Usuario usuario = usuarioRepository.findByNomeDeUsuarioAndSenha(nomeDeUsuario, generateMD5(senha));

        if (usuario == null) {
            throw new NotFoundException(messageSource.getMessage("usuarioNaoEncontrado", null, null));
        }        
        
        return getToken(usuario);
    }
    
    private String getNomeDeUsuario(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_TOKEN_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();   
    }
    
    private String getToken(Usuario usuario) {
        long nowMilliSeconds = System.currentTimeMillis();
        long expirationTime = JWT_TOKEN_EXPIRATION_TIME.longValue();
        Date now = new Date(nowMilliSeconds);

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, JWT_TOKEN_KEY)
                .setExpiration(new Date(nowMilliSeconds + expirationTime))
                .setIssuedAt(now)
                .setSubject(usuario.getNomeDeUsuario())
                .compact();
    }
    
    private String generateMD5(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(text.getBytes(), 0, text.length());
        
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }
}
