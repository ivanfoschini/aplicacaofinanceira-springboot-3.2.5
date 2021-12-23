package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ForbiddenException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.UnauthorizedException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Papel;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Servico;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Usuario;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ServicoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.UsuarioRepository;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MessageSource messageSource;

    private static final String JWT_TOKEN_KEY = "LATOSENSU";
    private static final Long JWT_TOKEN_EXPIRATION_TIME = 86400000L; //Um dia em milisegundos

    public boolean authorize(String requestUri, String token) throws ForbiddenException {
        String nomeDeUsuario = getNomeDeUsuarioFromToken(token);
        Usuario usuario = usuarioRepository.findByNomeDeUsuario(nomeDeUsuario);
        Servico servico = servicoRepository.findByUri(requestUri);
        
        if (usuario != null && servico != null) {
            for (Papel papelDoUsuario: usuario.getPapeis()) {
                for (Papel papelAssociadoAoServico: servico.getPapeis()) {
                    if (papelDoUsuario.getNome().equals(papelAssociadoAoServico.getNome())) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    public String login(String nomeDeUsuario, String senha) throws UnauthorizedException, NoSuchAlgorithmException {
        if (nomeDeUsuario == null || senha == null) {
            throw new UnauthorizedException();
        }
        
        Usuario usuario = usuarioRepository.findByNomeDeUsuarioAndSenha(nomeDeUsuario, generateMD5(senha));

        if (usuario == null) {
            throw new UnauthorizedException();
        }        
        
        return generateToken(usuario.getNomeDeUsuario());
    }

    private String generateToken(String nomeDeUsuario) {
        long nowMilliSeconds = System.currentTimeMillis();
        long expirationTime = JWT_TOKEN_EXPIRATION_TIME;
        Date now = new Date(nowMilliSeconds);

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, JWT_TOKEN_KEY)
                .setExpiration(new Date(nowMilliSeconds + expirationTime))
                .setIssuedAt(now)
                .setSubject(nomeDeUsuario)
                .compact();
    }

    private String generateMD5(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(text.getBytes(), 0, text.length());

        return new BigInteger(1, messageDigest.digest()).toString(16);
    }

    private String getNomeDeUsuarioFromToken(String token) throws ForbiddenException {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_TOKEN_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new ForbiddenException();
        }
    }
}
