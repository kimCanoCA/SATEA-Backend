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
            
            //  email con el que inicia sesión en el frontend y contraseña .
            
            consejero.setEmail("carlos.perez@example.com"); 
            consejero.setPassword(passwordEncoder.encode("clave123")); // Contraseña encriptada 
            
            //  Asigna el rol 
            consejero.setRol("CONSEJERO"); 

            // Datos adicionales consejero
            consejero.setNombre("Carlos Pérez");
            consejero.setUsername("carlos.perez");
            consejero.setFecha_creacion(new Date());
            
            consejeroRepository.save(consejero);
            System.out.println(">>> Consejero de prueba creado exitosamente.");
        }
    }
}