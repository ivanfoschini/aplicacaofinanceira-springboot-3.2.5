package br.ufscar.dc.latosensu.aplicacaofinanceira.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class SecurityUtil {

    private static final String JWT_TOKEN_KEY = "LATOSENSU";
    private static final Long JWT_TOKEN_EXPIRATION_TIME = 86400000L; //Um dia em milisegundos  
    
    public String getNomeDeUsuario(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_TOKEN_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();   
    }
    
    public String getToken(String nomeDeUsuario) {
        long nowMilliSeconds = System.currentTimeMillis();
        long expirationTime = JWT_TOKEN_EXPIRATION_TIME.longValue();
        Date now = new Date(nowMilliSeconds);

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, JWT_TOKEN_KEY)
                .setExpiration(new Date(nowMilliSeconds + expirationTime))
                .setIssuedAt(now)
                .setSubject(nomeDeUsuario)
                .compact();
    }
    
    public String generateMD5(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(text.getBytes(), 0, text.length());
        
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }
}
