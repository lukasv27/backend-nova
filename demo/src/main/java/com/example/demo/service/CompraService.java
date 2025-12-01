package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CompraRequestDTO;
import com.example.demo.model.Compra;
import com.example.demo.model.CompraItem;
import com.example.demo.model.Persona;
import com.example.demo.model.Product;
import com.example.demo.repository.CompraRepository;
import com.example.demo.repository.PersonaRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepo;

    @Autowired
    private PersonaRepository personaRepo;

    @Autowired
    private ProductRepository productRepo;

    public Compra crearCompra(CompraRequestDTO request) {

        Persona persona = personaRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Compra compra = new Compra();
        compra.setPersona(persona);

        List<CompraItem> itemsCompra = new ArrayList<>();
        int total = 0;

        for (CompraRequestDTO.ItemRequest itemReq : request.getItems()) {

            Product p = productRepo.findById(itemReq.productId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            int precioUnitario = Integer.parseInt(p.getPrice());
            int subtotal = precioUnitario * itemReq.quantity;

            CompraItem item = new CompraItem();
            item.setCompra(compra);
            item.setProducto(p);
            item.setCantidad(itemReq.quantity);
            item.setPrecioSubtotal(subtotal);

            total += subtotal;
            itemsCompra.add(item);
        }

        compra.setItems(itemsCompra);
        compra.setTotal(total);

        return compraRepo.save(compra);
    }

    public List<Compra> obtenerComprasUsuario(Long userId) {
        return compraRepo.findByPersonaId(userId);
    }
}
