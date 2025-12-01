package com.example.demo.dto;

import java.util.List;

public class CompraRequestDTO {

    private Long userId;
    private List<ItemRequest> items;

    public static class ItemRequest {
        public Long productId;
        public Integer quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ItemRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemRequest> items) {
        this.items = items;
    }
}
