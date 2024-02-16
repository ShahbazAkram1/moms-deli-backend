package com.momsdeli.online.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.momsdeli.online.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class JwtHelper {

    @Value("${jwt.expiration}")
    private String jwtExpirationMs;
    @Value("${jwt.secret}")
    private String secretKey;
    @Autowired(required = false)
    private HttpServletRequest request;

    @PostConstruct
    private void init() {
        try {
            secretKey = EncryptionUtils.encryptAES(secretKey);
            System.out.println("secret Key" + secretKey);
        } catch (Exception e) {
            // Log the exception
           log.info("Error during JWTHelper initialization");
        }
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

    public String generateToken(User userRespDTO) {
        // Create JWT claims
        Claims claims = Jwts.claims().setSubject(userRespDTO.getEmail());
        claims.put("user", userRespDTO);
        String header = request.getHeader("User-Agent");

        // Parse jwtExpirationMs as a long value representing milliseconds
        long expirationMillis = Long.parseLong(jwtExpirationMs);

        // Set current time and expiration time
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMillis);

        // Logging
        log.info("Generating token for user: {}", userRespDTO.getEmail());
        log.debug("Current time: {}", now);
        log.debug("Expiration time: {}", expirationDate);

        // Build JWT
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .setSubject(userRespDTO.getFirstName())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.debug("Generated JWT token: {}", jwt);

        return jwt;
    }


    public String getAgentFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("User-Agent", String.class);
    }

    public String getTerminalFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("Ip-Address", String.class);
    }


    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;  // Token is valid and not expired
        } catch (ExpiredJwtException expiredJwtException) {
            log.info("Token expired");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            log.info("Unsupported token");
        } catch (MalformedJwtException malformedJwtException) {
            log.info("Malformed token");
        } catch (Exception e) {
            log.info("Invalid token");
        }
        return false; // Token is either expired or invalid
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

    public User extractUserRespDToFromToken(String token) throws JsonProcessingException {
        Claims claims = Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        Object o = claims.get("user");
        ObjectMapper objectMapper = new ObjectMapper();
        String claimsString = objectMapper.writeValueAsString(o);
        User user = objectMapper.readValue(claimsString, User.class);
        return user;
    }
}

