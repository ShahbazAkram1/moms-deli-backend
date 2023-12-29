///**
// * Author: Shahbaz Ali
// * Email: shahbazkhaniq@gmail.com
// * Date: 12/27/2023$
// * Time: 5:54 PM$
// * Project Name: moms_deli_backend$
// */
//
//
//package com.momsdeli.online.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import jakarta.annotation.PostConstruct;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import java.security.Key;
//import io.jsonwebtoken.security.Keys;
//
//import java.util.Date;
//
//@Component
//@Data
//@Slf4j
//public class JwtUtil {
//
//
//    @Value("${jwt.secret}")
//    private String secret;
//    private Key key;
//
//    private final String AUTHORIZATION = "Authorization";
//    private final String BEARER = "Bearer ";
//
//    @PostConstruct
//    public void init() {
//        key = Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    public String extractUsername(String token) {
//        return getAllClaimsFromToken(token).getSubject();
//
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    private Claims getAllClaimsFromToken(String token) {
//
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception ex) {
//            log.error("Error extracting claims from JWT: ", ex);
//            throw new JwtException("Error extracting claims from JWT", ex);
//        }
//    }
//
//    private boolean isTokenExpired(String token) {
//        final Date expiration = getAllClaimsFromToken(token).getExpiration();
//        return expiration.before(new Date());
//    }
//
//}
