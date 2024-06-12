package com.shyam.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private String secretKey = "4a08d5f936726d01008151fb5c191ea03ab569a62c4aaf203ade719ea1c8b6e4";

    // generate secret key
    public SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // generate token
    public String generateJwtToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts
                .builder()
                .signWith(getSecretKey())
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 min
                .compact();
    }

    public String generateJwtToken(UserDetails userDetails) {
        return generateJwtToken(userDetails, new HashMap<>());
    }

    // extract claims
    public Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // extract single claim
    public <T> T getSingleClaim(String token, Function<Claims, T> resolver) {
        Claims claims = getAllClaims(token);
        return resolver.apply(claims);
    }

    // util methods
    public String getUsername(String token) {
        return getSingleClaim(token, Claims::getSubject);
    }

    //validation method
    public boolean isValidToken(String email, UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        return userEmail.equals(email);
    }

}
