package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CompraRequestDTO;
import com.example.demo.model.Compra;
import com.example.demo.service.CompraService;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping
    public Compra crearCompra(@RequestBody CompraRequestDTO request) {
        return compraService.crearCompra(request);
    }

    @GetMapping("/usuario/{id}")
    public List<Compra> historial(@PathVariable Long id) {
        return compraService.obtenerComprasUsuario(id);
    }
}
