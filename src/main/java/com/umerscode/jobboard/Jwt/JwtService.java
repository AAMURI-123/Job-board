package com.umerscode.jobboard.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {


    private final static String SECRET_KEY = String.valueOf(MacProvider.generateKey());


    public String getUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public Date getExpirationDate(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateJwt(UserDetails userDetails){
        return createJwt(new HashMap<>(),userDetails.getUsername());
    }

    private String createJwt(HashMap<Object, Object> map, String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer(username)
                .setExpiration(new Date(System.currentTimeMillis()+1000 *60 *24))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = getUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }


}
