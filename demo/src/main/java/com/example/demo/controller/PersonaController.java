package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegistroDTO;
import com.example.demo.model.Persona;
import com.example.demo.model.Role;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.PersonaService;

@RestController
@RequestMapping("/autentificacion")
@CrossOrigin(origins = "http://localhost:5173")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint para registrar usuario
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroDTO dto) {
        try {
            Persona persona = new Persona();
            persona.setNombre(dto.getNombre());
            persona.setApellido(dto.getApellido());
            persona.setEmail(dto.getEmail());
            persona.setPassword(dto.getPassword());
            persona.setRol(Role.CLIENTE); // Asignar rol CLIENTE por defecto en el registro

            Persona nuevaPersona = personaService.registrarUsuario(persona);
            return ResponseEntity.status(201).body(nuevaPersona); // 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
        }
    }

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            Persona persona = personaService.obtenerPorEmail(dto.getEmail());

            if (persona.getPassword().trim().equals(dto.getPassword().trim())) {
                String token = jwtUtil.generarToken(persona);

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("email", persona.getEmail());
                response.put("rol", persona.getRol().name());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Credenciales inválidas");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint opcional: obtener usuario por email
    @GetMapping("/usuario/{email}")
    public ResponseEntity<?> obtenerPorEmail(@PathVariable String email) {
        try {
            Persona persona = personaService.obtenerPorEmail(email);
            return ResponseEntity.ok(persona); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // 404 Not Found
        }
    }
    

    
}
