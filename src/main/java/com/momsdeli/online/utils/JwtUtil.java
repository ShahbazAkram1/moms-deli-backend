package com.momsdeli.online.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.momsdeli.online.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private String jwtExpirationMs;
    @Value("${jwt.secret}")
    private String secretKey;
    @Autowired
    private HttpServletRequest request;


    @PostConstruct
    private void init() throws Exception {
        secretKey = EncryptionUtils.encryptAES(secretKey);
    }


    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateToken(User userRespDTO){
        // Create JWT claims
        Claims claims = Jwts.claims().setSubject(userRespDTO.getEmail());

        claims.put("user", userRespDTO);
        String header = request.getHeader("Authorization");
        claims.put("Authorization",header);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + (Integer.parseInt(jwtExpirationMs)*60));

        // Build JWT
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .setSubject(userRespDTO.getFirstName())
                .signWith(SignatureAlgorithm.HS256, secretKey);

        // Generate the JWT token
        String jwt = jwtBuilder.compact();
        return jwt;
    }






    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            System.out.println("JWT token is expired");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            System.out.println("Unsupported JWT token");
        } catch (MalformedJwtException malformedJwtException) {
            System.out.println("JWT token is manipulated");
        }
        return false;
    }

    public String extractUsername(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Date extractExpiration(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }

    public User extractUserFromToken(String token) throws JsonProcessingException {
        Claims claims = Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        Object o = claims.get("user");
        ObjectMapper objectMapper = new ObjectMapper();
        String claimsString = objectMapper.writeValueAsString(o);
        User userRespDTO = objectMapper.readValue(claimsString, User.class);
        return userRespDTO;
    }

}

