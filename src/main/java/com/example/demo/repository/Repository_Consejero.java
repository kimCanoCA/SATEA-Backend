package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Consejero;
import java.util.Optional;



public interface Repository_Consejero extends JpaRepository<Consejero, Long>{
	 Optional<Consejero> findByUsername(String username);
	 Optional<Consejero> findByEmail(String email);

}
