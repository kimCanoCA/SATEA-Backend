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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private Repository_Consejero consejeroRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Busca al consejero por su email (que usamos como username)
        Consejero consejero = consejeroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + username));

        // Crea la lista de permisos, añadiendo el prefijo "ROLE_" al rol guardado en la BD.
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + consejero.getRol())
        );

        // Devuelve el objeto UserDetails que Spring Security entiende.
        return new User(
            consejero.getEmail(), 
            consejero.getPassword(), 
            authorities
        );
    }
}