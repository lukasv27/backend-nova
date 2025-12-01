package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


   @RestController
@RequestMapping("/products") // rutas públicas base
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ============================
    // RUTAS PÚBLICAS (CLIENTE)
    // ============================
    @GetMapping
    public List<ProductDTO> getProductsPublic() {
        return productService.getProducts();
    }

    @GetMapping("/category/{category}")
    public List<ProductDTO> getProductsByCategoryPublic(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    // ============================
    // RUTAS ADMIN (ADMINISTRADOR)
    // ============================
    @RestController
    @RequestMapping("/admin/products")
    public static class AdminController {

        @Autowired
        private ProductService productService;

        @PostMapping
        public ResponseEntity<?> createProduct(
                @RequestParam String name,
                @RequestParam String price,
                @RequestParam String category,
                @RequestParam List<String> sizes,
                @RequestParam MultipartFile image) {
            try {
                productService.saveProduct(name, price, category, sizes, image);
                return ResponseEntity.ok("Producto creado");
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error guardando el producto");
            }
        }

        @GetMapping
        public List<ProductDTO> getProductsAdmin() {
            return productService.getProducts();
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> updateProduct(
                @PathVariable Long id,
                @RequestParam String name,
                @RequestParam String price,
                @RequestParam String category,
                @RequestParam List<String> sizes,
                @RequestParam(required = false) MultipartFile image) {
            try {
                productService.updateProduct(id, name, price, category, sizes, image);
                return ResponseEntity.ok("Producto actualizado");
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error actualizando producto");
            } catch (RuntimeException e) {
                return ResponseEntity.status(404).body("Producto no encontrado");
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
            try {
                productService.deleteProduct(id);
                return ResponseEntity.ok("Producto eliminado");
            } catch (RuntimeException e) {
                return ResponseEntity.status(404).body("Producto no encontrado");
            }
        }
    }
}
