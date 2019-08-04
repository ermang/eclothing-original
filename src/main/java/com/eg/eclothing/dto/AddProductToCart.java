package com.eg.eclothing.dto;

public class AddProductToCart {
    public long productId;
    public long stockId;

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }
}
