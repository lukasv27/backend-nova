package com.example.demo.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // -------------------------
    // GUARDAR PRODUCTO
    // -------------------------
    public Product saveProduct(String name, String price, String category, List<String> sizes, MultipartFile image)
            throws IOException {

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        product.setSizes(sizes); // ahora es lista de tallas
        product.setImage(image.getBytes());

        return productRepository.save(product);
    }

    // -------------------------
    // EDITAR PRODUCTO
    // -------------------------
    public Product updateProduct(Long id, String name, String price, String category, List<String> sizes, MultipartFile image)
            throws IOException {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        product.setSizes(sizes);

        if (image != null && !image.isEmpty()) {
            product.setImage(image.getBytes());
        }

        return productRepository.save(product);
    }

    // -------------------------
    // ELIMINAR PRODUCTO
    // -------------------------
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("El producto no existe");
        }
        productRepository.deleteById(id);
    }

    // -------------------------
    // LISTAR PRODUCTOS (DTO)
    // -------------------------
    public List<ProductDTO> getProducts() {

        return productRepository.findAll()
                .stream()
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setCategory(product.getCategory());
                    dto.setSizes(product.getSizes());

                    String base64Image = "data:image/jpeg;base64," +
                            Base64.getEncoder().encodeToString(product.getImage());

                    dto.setImageBase64(base64Image);

                    return dto;
                }).collect(Collectors.toList());
    }

    // -------------------------
    // FILTRAR POR CATEGORÍA
    // -------------------------
    public List<ProductDTO> getProductsByCategory(String category) {

        return productRepository.findByCategory(category)
                .stream()
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setCategory(product.getCategory());
                    dto.setSizes(product.getSizes());

                    String base64Image = "data:image/jpeg;base64," +
                            Base64.getEncoder().encodeToString(product.getImage());

                    dto.setImageBase64(base64Image);

                    return dto;
                }).collect(Collectors.toList());
    }

    // -------------------------
    // OBTENER PRODUCTO POR ID
    // -------------------------
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }
}
