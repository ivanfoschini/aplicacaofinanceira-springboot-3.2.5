package br.ufscar.dc.latosensu.aplicacaofinanceira.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.ForbiddenException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.exception.UnauthorizedException;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Papel;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Servico;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Usuario;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.ServicoRepository;
import br.ufscar.dc.latosensu.aplicacaofinanceira.repository.UsuarioRepository;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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

    // Uma chave secreta qualquer, para ser utilizada para criptografar o token a ser gerado.
    // Com isso, apenas quem tiver esta chave poderá gerar um token válido.
    // Para simplificar, esta chave será deixada no próprio código, como uma constante.
    // O ideal seria que este valor fosse recuperado de uma variável de ambiente ou de um repositório seguro.
    // O valor da constante corresponde a "BrUFSCarDCLatoSensuAplicacaoFinanceira" em Base64.
    private static final String JWT_TOKEN_SECRET_KEY = "QnJVRlNDYXJEQ0xhdG9TZW5zdUFwbGljYWNhb0ZpbmFuY2VpcmE=";
    private static final Long JWT_TOKEN_EXPIRATION_TIME = 86400000L; //Um dia em milisegundos

    public boolean authorize(String token, String requestUri) throws ForbiddenException {
        String nomeDeUsuario = getNomeDeUsuarioFromToken(token);
        Usuario usuario = usuarioRepository.findByNomeDeUsuario(nomeDeUsuario);

        if (usuario != null) {
            for (Papel papel : usuario.getPapeis()) {
                for (Servico servico: papel.getServicos()) {
                    if (servico.getUri().equals(requestUri)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public String login(String nomeDeUsuario, String senha) throws UnauthorizedException {
        if (nomeDeUsuario == null || senha == null) {
            throw new UnauthorizedException();
        }

        Usuario usuario = usuarioRepository.findByNomeDeUsuario(nomeDeUsuario);

        if (usuario == null) {
            throw new UnauthorizedException();
        }

        BCrypt.Result result = BCrypt.verifyer().verify(senha.toCharArray(), usuario.getSenha());

        if (!result.verified) {
            throw new UnauthorizedException();
        }
        
        return generateToken(usuario.getNomeDeUsuario());
    }

    public String generateToken(String nomeDeUsuario) {
        SecretKey secretKey = getSecretKey();
        long nowMilliSeconds = System.currentTimeMillis();
        long expirationTime = JWT_TOKEN_EXPIRATION_TIME;
        Date now = new Date(nowMilliSeconds);

        return Jwts.builder()
            .id(UUID.randomUUID().toString())
            .subject(nomeDeUsuario)
            .issuedAt(now)
            .expiration(new Date(nowMilliSeconds + expirationTime))
            .signWith(secretKey)
            .compact();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_TOKEN_SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String getNomeDeUsuarioFromToken(String token) throws ForbiddenException {
        SecretKey secretKey = getSecretKey();

        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        } catch (Exception e) {
            throw new ForbiddenException();
        }
    }

    /**
    // Este método não é utilizado, mas foi mantido para mostrar como foram geradas as senhas para os usuários
    private String generateBCrypt(String text) {
        return BCrypt.withDefaults().hashToString(10, text.toCharArray());
    }
    */
}
