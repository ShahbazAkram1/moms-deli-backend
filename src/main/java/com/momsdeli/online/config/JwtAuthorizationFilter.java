/*
package com.momsdeli.online.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.momsdeli.online.model.User;
import com.momsdeli.online.repository.UserRepository;
import com.momsdeli.online.service.UserService;
import com.momsdeli.online.utils.Constants;
import com.momsdeli.online.utils.Encryption;
import com.momsdeli.online.utils.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JwtAuthorizationFilter extends OncePerRequestFilter {
    // Your existing code implementation
    public JwtHelper jwtHelper;
    public UserRepository userRepository;
    public UserService userService;



    public JwtAuthorizationFilter(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        Boolean isValidAccessToken = Boolean.FALSE;
        final String ACCESS_TOKEN = request.getHeader("ACCESS-TOKEN");
        final String ORIGINAL_ACCESS_TOKEN = "AllowThisRequestAsItIsComingFromGATEWAY";
        if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() < 10) {
//            sendResponse(request, response, chain, "Service is not available", null, HttpStatus.FAILED_DEPENDENCY, HttpStatus.NOT_FOUND.value());
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().write("Service Not Available");
            System.out.println("1. Service Not Available");
            response.flushBuffer();

            return;
        } else {
            String accessToken = "";
            try {
                accessToken = Encryption.decrypt(ACCESS_TOKEN);
            } catch (Exception e) {
//                sendResponse(request, response, chain, "Service is not available", null, StatusEnum.FAILURE, HttpStatus.NOT_FOUND.value());
                return;
            }
            if (accessToken.equals(ORIGINAL_ACCESS_TOKEN)) {
                isValidAccessToken = Boolean.TRUE;

            } else {
                System.out.println("Service not ");
//                sendResponse(request, response, chain, "Service is not available", null, StatusEnum.FAILURE, HttpStatus.NOT_FOUND.value());
                return;
            }
        }
        for (String str : Constants.WHITELIST_ENDPOINTS) {
            if (request.getRequestURI().contains(str)) {
                response.setStatus(200);
                chain.doFilter(request, response);
                return;
            }
        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("INVALID TOKEN");
            return;
        }
        jwt = authHeader.substring(7);

        if (jwtHelper.validateToken(jwt)) {
            String agentFromToken = jwtHelper.getAgentFromToken(jwt);
            if (agentFromToken.equals(request.getHeader("User-Agent"))) {
                User userRespDTO = jwtHelper.extractUserRespDToFromToken(jwt);

                if (true) {
                    if (isValidAccessToken) {
                        // Set the authenticated user in the security context
                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(userRespDTO, null, null));
                        response.setStatus(200);
                        chain.doFilter(request, response);
                    } else {
//                        sendResponse(request, response, chain, "Service is not available", null, StatusEnum.FAILURE, HttpStatus.UNAUTHORIZED.value());
                    }

                }
            } else {
                throw new Exception("Invalid User-agent. Regenerate your token");
            }
        }
    }

    private void sendResponse(HttpServletRequest request, HttpServletResponse response, FilterChain chain, String message, Object data, Long code, Integer httpStatusCode) {
        ObjectMapper objectMapper = new ObjectMapper();

        String statusDtoString = null;
        try {
            response.setStatus(httpStatusCode);
            response.setContentType("application/json");
            response.getWriter().write("NOT FOUND");
            response.flushBuffer();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}*/
