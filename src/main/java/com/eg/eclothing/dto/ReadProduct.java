package com.eg.eclothing.dto;

import java.math.BigDecimal;
import java.util.List;

public class ReadProduct {
    public Long baseProductId;
    public String baseProductName;
    public String baseProductCategory;
    public Long productId;
    public String color;
    public BigDecimal price;
    public List<String> imagePaths;
}
