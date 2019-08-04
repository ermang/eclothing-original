package com.eg.eclothing.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreateProduct {
    public Long baseProductId;
    public String color;
    public BigDecimal price;
    public List<String> base64Images;
}
