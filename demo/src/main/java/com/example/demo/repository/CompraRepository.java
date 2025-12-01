package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByPersonaId(Long personaId);
}
