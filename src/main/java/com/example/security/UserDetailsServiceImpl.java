package com.example.security;

import com.example.demo.repository.Repository_Consejero;
import com.example.model.Consejero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Esta clase es una implementación de la interfaz UserDetailsService de Spring Security.
 * Actúa como un adaptador entre el modelo de datos de tu aplicación (Consejero)
 * y el modelo de usuario que Spring Security necesita (UserDetails).
 * Su única responsabilidad es cargar los datos de un usuario desde la base de datos
 * a partir de su nombre de usuario.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyectamos el repositorio para poder acceder a la base de datos y buscar consejeros.
    @Autowired
    private Repository_Consejero consejeroRepository;

    /**
     * Este es el único método que debemos implementar de la interfaz UserDetailsService.
     * Spring Security lo llamará automáticamente durante el proceso de autenticación.
     * Por ejemplo, lo usa el JwtRequestFilter después de extraer el username del token.
     *
     * @param username El nombre de usuario (en este caso, el email) que se quiere buscar.
     * @return un objeto UserDetails que contiene la información del usuario.
     * @throws UsernameNotFoundException si no se encuentra ningún usuario con ese username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. BUSCAR EL USUARIO EN LA BASE DE DATOS
        // Utilizamos el repositorio para buscar un 'Consejero' por su email.
        // El método .orElseThrow() es una forma limpia de manejar el caso en que el usuario no existe.
        // Si no lo encuentra, lanza la excepción que Spring Security espera.
        Consejero consejero = consejeroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + username));

        // 2. CONVERTIR LOS ROLES A "GrantedAuthority"
        // Spring Security no entiende de strings como "ADMIN" o "USER".
        // Necesita que los roles estén envueltos en objetos de tipo GrantedAuthority.
        // Es una convención de Spring Security añadir el prefijo "ROLE_" a los roles.
        // Esto es crucial para que funcionen las anotaciones como @PreAuthorize("hasRole('ADMIN')").
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + consejero.getRol())
        );

        // 3. CREAR Y DEVOLVER EL OBJETO UserDetails
        // Creamos un objeto 'User' (que es una implementación de UserDetails) con los datos necesarios:
        // - El nombre de usuario (email).
        // - La contraseña (ya debe estar codificada/hasheada en la base de datos).
        // - La lista de sus permisos (authorities).
        // Este es el objeto que Spring Security utilizará para completar la autenticación.
        return new User(
                consejero.getEmail(),
                consejero.getPassword(),
                authorities
        );
    }
}