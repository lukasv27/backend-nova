package com.example.demo.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.demo.model.Persona;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Clave secreta (mínimo 32 caracteres para HS256)
    private final String SECRET = "clave-super-segura-de-al-menos-32-caracteres";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Genera el token con email y rol
    public String generarToken(Persona persona) {
        return Jwts.builder()
                .setSubject(persona.getEmail())
                .claim("rol", persona.getRol().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrae todos los claims del token
    public Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extrae el email (subject)
    public String extraerEmail(String token) {
        return extraerClaims(token).getSubject();
    }

    // Extrae el rol desde los claims
    public String extraerRol(String token) {
        Object rol = extraerClaims(token).get("rol");
        return rol != null ? rol.toString() : null;
    }

    // Verifica si el token está expirado
    public boolean tokenExpirado(String token) {
        Date exp = extraerClaims(token).getExpiration();
        return exp.before(new Date());
    }
}


