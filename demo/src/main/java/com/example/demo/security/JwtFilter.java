package com.example.demo.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.Persona;
import com.example.demo.service.PersonaService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PersonaService personaService;

    public JwtFilter(JwtUtil jwtUtil, PersonaService personaService) {
        this.jwtUtil = jwtUtil;
        this.personaService = personaService;
    }

    

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
                

        String authHeader = request.getHeader("Authorization");

        System.out.println("Authorization header: " + authHeader);

        // Solo procesamos si viene un Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String email = jwtUtil.extraerEmail(token);
                String rol = jwtUtil.extraerRol(token);

                if (email != null && rol != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // opcional: verificar existencia del usuario
                    Persona persona = personaService.obtenerPorEmail(email);

                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            persona, // principal = objeto Persona
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + rol)));

               
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("Email: " + email);
                    System.out.println("Rol claim (del token): " + rol);
                    System.out.println("Rol en BD (persona.getRol()): " + persona.getRol());
                    System.out.println("Authorities: " + authentication.getAuthorities());
       }
            } catch (Exception e) {
                // Token inválido o expirado: no autenticamos
                SecurityContextHolder.clearContext();
                
            }
        }

        chain.doFilter(request, response);
    }
}
