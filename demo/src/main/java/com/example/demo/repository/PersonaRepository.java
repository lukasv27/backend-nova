package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Persona;

public interface  PersonaRepository extends JpaRepository<Persona, Long> {
     Optional<Persona> findByEmail(String email);

    
}
