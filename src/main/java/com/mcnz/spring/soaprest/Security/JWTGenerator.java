package com.mcnz.spring.soaprest.Security;


import com.mcnz.spring.soaprest.Security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.UUID;

import static com.mcnz.spring.soaprest.Security.SecurityConstants.JWT_Secret;

@Component
public class JWTGenerator {
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + SecurityConstants.JWT_Expiration);
        String jti = UUID.randomUUID().toString();


        String token  = Jwts.builder()
                .setId(jti)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_Secret)
                .compact();
        return token;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_Secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_Secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT is expired or invalid");
        }
    }
    public String getJtiFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_Secret)
                .parseClaimsJws(token)
                .getBody()
                .getId();
    }

}
