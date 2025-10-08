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

/**
 * Filtro de seguridad que intercepta todas las peticiones HTTP para procesar el token JWT.
 * Hereda de OncePerRequestFilter para garantizar que se ejecuta solo una vez por cada petición.
 * Es el punto de entrada para la autenticación basada en tokens en cada solicitud a un endpoint protegido.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // Inyección de dependencias de los servicios que necesitamos.
    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Servicio para cargar los detalles del usuario desde la BD.

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Clase de utilidad para crear, validar y leer tokens JWT.

    /**
     * Este es el método principal del filtro. Se ejecuta para cada petición entrante.
     * Su lógica es:
     * 1. Extraer el token JWT de la cabecera "Authorization".
     * 2. Validar el token.
     * 3. Si el token es válido, establecer la autenticación en el contexto de seguridad de Spring.
     *
     * @param request La petición HTTP entrante.
     * @param response La respuesta HTTP.
     * @param chain La cadena de filtros de seguridad.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        
        // 1. OBTENER EL TOKEN DE LA CABECERA
        // Se busca la cabecera "Authorization" en la petición. Es el estándar para enviar credenciales.
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Comprueba si la cabecera existe y si sigue el formato "Bearer <token>"
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrae el token JWT, quitando el prefijo "Bearer " (7 caracteres).
            jwt = authorizationHeader.substring(7);
            try {
                // Utiliza el JwtTokenProvider para extraer el nombre de usuario del token.
                username = jwtTokenProvider.getUsernameFromToken(jwt);
            } catch (Exception e) {
                // Si el token está malformado, ha expirado o es inválido, se captura la excepción.
                // No se hace nada más, la petición continuará sin autenticación.
                System.out.println("[ERROR]: Fallo al extraer username del token. Causa: " + e.getMessage());
            }
        } else {
            // Si no hay cabecera "Authorization" o no tiene el formato correcto, se informa por consola.
            logger.warn("El JWT Token no comienza con 'Bearer ' o no fue encontrado.");
        }

        // 2. VALIDAR EL TOKEN Y ESTABLECER LA AUTENTICACIÓN
        // Se ejecuta solo si hemos extraído un nombre de usuario Y no hay ya una autenticación en el contexto.
        // La segunda condición (getAuthentication() == null) es importante para no sobreescribir una autenticación ya existente.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carga los detalles del usuario (roles, permisos, etc.) desde la base de datos
            // usando el nombre de usuario extraído del token.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Valida el token: comprueba que la firma sea correcta, que no haya expirado
            // y que el usuario del token coincida con los UserDetails cargados.
            if (jwtTokenProvider.validateToken(jwt, userDetails)) {

                // Si el token es válido, se crea un objeto de autenticación de Spring Security.
                // Este objeto representa al usuario autenticado.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                // Se adjuntan detalles de la petición web (como la IP) al objeto de autenticación.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ¡PASO CLAVE! Se establece el objeto de autenticación en el contexto de seguridad de Spring.
                // A partir de este momento, Spring Security considera al usuario como autenticado para esta petición.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        
        // 3. CONTINUAR CON LA CADENA DE FILTROS
        // Cede el control al siguiente filtro en la cadena de seguridad de Spring.
        // Si no se hace esto, la petición se detiene aquí y nunca llegará al controlador.
        chain.doFilter(request, response);
    }
}