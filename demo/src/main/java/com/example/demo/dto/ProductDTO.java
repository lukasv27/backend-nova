package com.example.demo.dto;

import java.util.Base64;
import java.util.List;

import com.example.demo.model.Product;

public class ProductDTO {

    private Long id;
    private String name;
    private String price;
    private String category;
    private List<String> sizes; // varias tallas
    private String imageBase64;

    // ------------------ GETTERS Y SETTERS ------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    // ------------------ MÉTODO AUXILIAR ------------------
    public static ProductDTO fromEntity(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setSizes(product.getSizes());

        if (product.getImage() != null) {
            dto.setImageBase64("data:image/jpeg;base64," +
                    Base64.getEncoder().encodeToString(product.getImage()));
        }

        return dto;
    }
}
