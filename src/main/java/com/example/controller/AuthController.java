package com.example.controller;

import com.example.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            // Intenta autenticar al usuario con el email y la contraseña
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Si las credenciales son incorrectas, lanza una excepción que devuelve un error 401
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", e);
        }

        // Si la autenticación es exitosa, carga los detalles del usuario
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        
        // Genera el token JWT
        final String token = jwtTokenProvider.generateToken(userDetails);
        
        // Devuelve el token en la respuesta
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

// --- Clases DTO (Asegúrate de tenerlas así) ---

class AuthRequest {
    private String email;
    private String password;

    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class AuthResponse {
    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}