package com.palacios_chacana.tcgstore.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class SecretGatewayFilter extends OncePerRequestFilter {

    @Value("${app.gateway.secret}")
    private String expectedSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String secretHeader = request.getHeader("X-Secret-Gateway");

        // La cabecera debe existir y coincidir exactamente con el secreto.
        // MessageDigest.isEqual compara en tiempo constante (evita ataques por temporización).
        if (!secretMatches(secretHeader)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write("Acceso denegado: Secreto de pasarela inválido o ausente");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean secretMatches(String header) {
        if (header == null || expectedSecret == null || expectedSecret.isBlank()) {
            return false;
        }
        return MessageDigest.isEqual(
                expectedSecret.getBytes(StandardCharsets.UTF_8),
                header.getBytes(StandardCharsets.UTF_8));
    }
}
