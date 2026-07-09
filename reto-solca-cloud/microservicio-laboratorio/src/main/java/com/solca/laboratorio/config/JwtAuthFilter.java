package com.solca.laboratorio.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class JwtAuthFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if ("OPTIONS".equalsIgnoreCase(method) ||
            path.equals("/api/") || path.startsWith("/api/actuator/") || path.startsWith("/api/auth/")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(401);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"status\":\"ERROR\",\"message\":\"Token de autenticacion requerido\"}");
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            httpResponse.setStatus(401);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"status\":\"ERROR\",\"message\":\"Token invalido o expirado\"}");
            return;
        }

        httpRequest.setAttribute("jwtUsername", jwtUtil.getUsernameFromToken(token));
        httpRequest.setAttribute("jwtRol", jwtUtil.getRolFromToken(token));
        httpRequest.setAttribute("jwtNombres", jwtUtil.getNombresFromToken(token));
        httpRequest.setAttribute("jwtApellidos", jwtUtil.getApellidosFromToken(token));

        chain.doFilter(request, response);
    }
}
