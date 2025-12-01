package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Persona;
import com.example.demo.repository.PersonaRepository;

@Service
public class PersonaService {
    
    @Autowired
    private PersonaRepository personaRepository;

    // Registro con validaciones
    public Persona registrarUsuario(Persona persona) {
        // nombre vacío
        if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre no puede estar vacío");
        }

        // apellido vacío
        if (persona.getApellido() == null || persona.getApellido().trim().isEmpty()) {
            throw new RuntimeException("El apellido no puede estar vacío");
        }

        // email vacío y con @
        if (persona.getEmail() == null || persona.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email no puede estar vacío");
        }
        if (!persona.getEmail().contains("@")) {
            throw new RuntimeException("El email debe contener '@'");
        }

        // password vacío
        if (persona.getPassword() == null || persona.getPassword().trim().isEmpty()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }

        // verificar si ya existe
        Optional<Persona> personaExistente = personaRepository.findByEmail(persona.getEmail());
        if (personaExistente.isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        return personaRepository.save(persona);
    }

    // Login 
    public boolean login(String email, String password) {
        Optional<Persona> personaOpt = personaRepository.findByEmail(email);
        return personaOpt.isPresent() && personaOpt.get().getPassword().equals(password);
    }

    // Obtener usuario por email
    public Persona obtenerPorEmail(String email) {
        return personaRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    
}
