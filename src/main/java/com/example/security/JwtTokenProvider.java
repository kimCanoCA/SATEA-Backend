package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.function.Function;

/**
 * Clase de utilidad (Componente de Spring) para manejar todas las operaciones relacionadas con JWT.
 * Se encarga de:
 * 1. Generar nuevos tokens para los usuarios.
 * 2. Extraer información (claims) de un token existente.
 * 3. Validar si un token es correcto y no ha expirado.
 */
@Component
public class JwtTokenProvider {

    // Inyecta la clave secreta desde el archivo de propiedades (ej: application.properties).
    // Esta clave es fundamental para firmar y verificar los tokens. ¡Debe ser secreta!
    @Value("${jwt.secret}")
    private String secret;

    // Inyecta el tiempo de expiración del token en segundos desde el archivo de propiedades.
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Genera un nuevo token JWT para un usuario autenticado.
     * @param userDetails Los detalles del usuario (obtenidos de la base de datos).
     * @return Un String que representa el token JWT compacto.
     */
    public String generateToken(UserDetails userDetails) {
        // Jwts.builder() es el constructor que nos permite crear el token paso a paso.
        return Jwts.builder()
                // 1. setSubject: Establece el "sujeto" del token. Generalmente es el username.
                .setSubject(userDetails.getUsername())
                // 2. setIssuedAt: Establece la fecha y hora en que se creó el token.
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 3. setExpiration: Establece la fecha y hora de caducidad. Se calcula sumando el tiempo de expiración.
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                // 4. signWith: ¡El paso más importante! Firma el token usando el algoritmo HS256 y nuestra clave secreta.
                // Esta firma garantiza que el token no ha sido modificado por nadie.
                .signWith(SignatureAlgorithm.HS256, secret)
                // 5. compact: Finaliza la creación y devuelve el token como un String en formato base64url.
                .compact();
    }

    /**
     * Valida si un token es correcto para un usuario específico.
     * @param token El token JWT a validar.
     * @param userDetails Los detalles del usuario contra los que se compara el token.
     * @return true si el token pertenece al usuario y no ha expirado, false en caso contrario.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        // Extrae el nombre de usuario del token.
        final String username = getUsernameFromToken(token);
        // Comprueba dos cosas:
        // 1. Que el nombre de usuario del token sea el mismo que el del UserDetails.
        // 2. Que el token no haya expirado.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrae el nombre de usuario (el "subject") del token.
     * @param token El token JWT.
     * @return El nombre de usuario.
     */
    public String getUsernameFromToken(String token) {
        // Usa el método genérico getClaimFromToken para obtener específicamente el "subject".
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Método privado que comprueba si un token ha expirado.
     * @param token El token JWT.
     * @return true si el token ha expirado, false si todavía es válido.
     */
    private Boolean isTokenExpired(String token) {
        // Obtiene la fecha de expiración del token.
        final Date expirationDate = getClaimFromToken(token, Claims::getExpiration);
        // Comprueba si la fecha de expiración es ANTERIOR a la fecha actual.
        return expirationDate.before(new Date());
    }

    /**
     * Método genérico y reutilizable para extraer cualquier información (un "claim") del token.
     * Utiliza una función (claimsResolver) para saber qué claim específico extraer.
     * @param token El token JWT del que se extraerá la información.
     * @param claimsResolver Una función que especifica qué claim obtener (ej. Claims::getSubject, Claims::getExpiration).
     * @return El claim solicitado.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        // Parsea el token para obtener todos los "claims" (el cuerpo o payload del JWT).
        // Este paso también valida la firma del token. Si la firma es inválida, lanzará una excepción.
        final Claims claims = Jwts.parser()
                                  .setSigningKey(secret) // Usa la clave secreta para verificar la firma.
                                  .parseClaimsJws(token) // Parsea y valida el token.
                                  .getBody(); // Obtiene el cuerpo con todos los claims.
        // Aplica la función recibida para extraer y devolver el claim deseado.
        return claimsResolver.apply(claims);
    }
}