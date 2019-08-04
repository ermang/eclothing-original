package com.eg.eclothing.dto;

import java.math.BigDecimal;
import java.util.List;

public class ReadStock {
    public long productId;
    public String productName;
    public String productCategory;
    public long stockId;
    public String color;
    public BigDecimal price;
    public Integer width;
    public Integer height;
    public String size;
    public Integer quantity;
    public List<String> imageURLs;
}
