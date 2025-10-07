package com.example.config;

import com.example.demo.repository.Repository_Consejero;
import com.example.model.Consejero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private Repository_Consejero consejeroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Solo crea el usuario si no existe ninguno en la base de datos
        if (consejeroRepository.count() == 0) {
            System.out.println(">>> Creando consejero de prueba...");

            Consejero consejero = new Consejero();
            
            // ¡IMPORTANTE! Usa exactamente el mismo email con el que inicias sesión en el frontend.
            // Por ejemplo, el que usaste en Postman.
            consejero.setEmail("carlos.perez@example.com"); 
            consejero.setPassword(passwordEncoder.encode("clave123")); // Contraseña encriptada
            
            // ESTA LÍNEA ES CORRECTA. Asigna el rol que configuramos en SecurityConfig.
            consejero.setRol("CONSEJERO"); 

            // Datos adicionales que ya tenías
            consejero.setNombre("Carlos Pérez");
            consejero.setUsername("carlos.perez");
            consejero.setFecha_creacion(new Date());
            
            consejeroRepository.save(consejero);
            System.out.println(">>> Consejero de prueba creado exitosamente.");
        }
    }
}