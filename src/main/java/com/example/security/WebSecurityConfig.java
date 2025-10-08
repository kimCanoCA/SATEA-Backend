package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Clase central de configuración de seguridad para la aplicación.
 * @Configuration indica que es una clase de configuración de Spring.
 * @EnableWebSecurity habilita la configuración de seguridad web de Spring Security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	

    // Inyectamos nuestro filtro personalizado para JWT.
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Este Bean define la cadena de filtros de seguridad y las reglas de autorización HTTP.
     * Es el corazón de la configuración de seguridad.
     * @param http El objeto HttpSecurity para configurar la seguridad.
     * @return La cadena de filtros de seguridad construida.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Configuración de CORS (Cross-Origin Resource Sharing)
            // Permite que tu frontend (ej. Angular en localhost:4200) se comunique con el backend.
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 2. Deshabilitación de CSRF (Cross-Site Request Forgery)
            // Es una protección para aplicaciones web tradicionales con sesiones.
            // Para APIs REST sin estado (stateless) como esta, no es necesario y se puede desactivar.
            .csrf(csrf -> csrf.disable())

            // 3. Reglas de autorización para las peticiones HTTP
            .authorizeHttpRequests(auth -> auth
            		 .requestMatchers(
                             "/swagger-ui.html",
                             "/swagger-ui/**",          // <-- Permite acceso a todos los recursos de la UI
                             "/v3/api-docs/**",         // <-- Permite acceso a la definición de la API
                             "/swagger-resources/**",   // <-- Necesario para algunas versiones
                             "/webjars/**"              // <-- Necesario para los recursos web estáticos
                         ).permitAll()	
                // Permite el acceso público al endpoint de autenticación para que cualquiera pueda iniciar sesión.
                .requestMatchers("/authenticate").permitAll()
                // Protege todas las rutas bajo "/api/v1/". Solo los usuarios con el rol "CONSEJERO" pueden acceder.
                .requestMatchers("/api/v1/**").hasRole("CONSEJERO")
                // Para cualquier otra petición no definida arriba, el usuario debe estar autenticado.
                .anyRequest().authenticated()
            )

            // 4. Gestión de sesiones sin estado (STATELESS)
            // ¡Esto es CRUCIAL para JWT! Le dice a Spring Security que no cree ni gestione sesiones HTTP.
            // Cada petición se autentica por sí misma con el token, sin depender de una sesión en el servidor.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 5. Añadir nuestro filtro JWT personalizado
            // Inserta el 'jwtRequestFilter' ANTES del filtro de autenticación estándar de Spring.
            // Así, cada petición pasa primero por nuestro filtro para validar el token JWT.
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Construye y devuelve la configuración de seguridad.
        return http.build();
    }

    /**
     * Este Bean expone el AuthenticationManager de Spring Security.
     * Es necesario para el proceso de autenticación manual en nuestro controlador de login.
     * @param authenticationConfiguration La configuración de autenticación de Spring.
     * @return El AuthenticationManager configurado.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Este Bean define la configuración de CORS para toda la aplicación.
     * Reemplaza la necesidad de un archivo WebConfig separado solo para esto.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Especifica los orígenes permitidos (la URL de tu frontend de Angular).
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        // Especifica los métodos HTTP permitidos (GET, POST, etc.).
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permite cualquier cabecera en la petición.
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Permite que el navegador envíe credenciales (como el token en la cabecera Authorization).
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuración a todas las rutas de la API ("/**").
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Este Bean le dice a Spring Security cómo codificar y verificar las contraseñas.
     * Es fundamental para la seguridad de las credenciales.
     * @return Una instancia de BCryptPasswordEncoder, el codificador recomendado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt es un algoritmo de hashing fuerte y adaptativo.
        // Automáticamente gestiona el "salting" para proteger contra ataques de rainbow table.
        return new BCryptPasswordEncoder();
    }
}