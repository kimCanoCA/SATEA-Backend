package com.example.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Usa la configuración de CORS definida en el bean de abajo
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. Deshabilita CSRF
            .csrf(csrf -> csrf.disable())
            
            // 3. Define las reglas de autorización para las rutas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/authenticate").permitAll() // Permite el login
                .requestMatchers("/api/v1/**").hasRole("CONSEJERO") // Protege tu API
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra ruta
            )
            
            // 4. Configura la gestión de sesión como STATELESS (sin estado)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 5. Añade tu filtro JWT antes del filtro de autenticación estándar
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    // ESTE BEAN REEMPLAZA TU ARCHIVO WebConfig.java
    // Aquí definimos qué orígenes, métodos y cabeceras están permitidos.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite peticiones desde tu URL de Angular
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        // Permite los métodos HTTP que necesitas
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permite todas las cabeceras
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Permite el envío de credenciales (cookies, tokens)
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuración a todas las rutas de tu aplicación
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
 // Este bean le enseña a Spring a crear un codificador de contraseñas.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}