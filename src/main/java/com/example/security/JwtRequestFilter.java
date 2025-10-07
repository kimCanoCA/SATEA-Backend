package com.example.security; // O el paquete donde esté tu archivo

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        
        // --- INICIO DE NUESTROS ESPÍAS ---
        System.out.println("\n====================================================");
        System.out.println(">>> JwtRequestFilter: Interceptando petición a: " + request.getRequestURI());

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            System.out.println("Paso 1: Cabecera 'Authorization' encontrada. Token: " + jwt.substring(0, 15) + "...");
            try {
                username = jwtTokenProvider.getUsernameFromToken(jwt);
                System.out.println("Paso 2: Username extraído del token: '" + username + "'");
            } catch (Exception e) {
                System.out.println("[ERROR]: Fallo al extraer username del token. Causa: " + e.getMessage());
            }
        } else {
            System.out.println("[AVISO]: No se encontró cabecera 'Authorization' o no empieza con 'Bearer '.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Paso 3: Cargando UserDetails para el usuario: '" + username + "'");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            System.out.println("Paso 4: UserDetails cargado. Permisos (Authorities): " + userDetails.getAuthorities());

            if (jwtTokenProvider.validateToken(jwt, userDetails)) {
                System.out.println("Paso 5: El token es VÁLIDO. Estableciendo autenticación en el contexto de seguridad.");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                System.out.println("[ERROR]: La validación del token ha fallado.");
            }
        } else {
            System.out.println("[AVISO]: El username es nulo o ya existe una autenticación en el contexto.");
        }
        
        System.out.println(">>> JwtRequestFilter: Cediendo el control al siguiente filtro.");
        System.out.println("====================================================");
        chain.doFilter(request, response);
    }
}