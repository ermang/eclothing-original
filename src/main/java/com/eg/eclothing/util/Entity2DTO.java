package com.eg.eclothing.util;

import com.eg.eclothing.dto.*;
import com.eg.eclothing.entity.BaseProduct;
import com.eg.eclothing.entity.Product;
import com.eg.eclothing.entity.ProductImage;
import com.eg.eclothing.entity.Stock;
import com.eg.eclothing.repo.ProductImageRepo;
import com.eg.eclothing.repo.projection.ImagePathOnly;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Entity2DTO {

    private final ProductImageRepo productImageRepo;

    public Entity2DTO(ProductImageRepo productImageRepo) {
        this.productImageRepo = productImageRepo;
    }

    public ReadProduct product2ReadProduct(Product p) {
        ReadProduct rp = new ReadProduct();
        rp.baseProductId= p.getBaseProduct().getId();
        rp.baseProductCategory = p.getBaseProduct().getCategory();
        rp.baseProductName = p.getBaseProduct().getName();
        rp.productId = p.getId();
        rp.color = p.getColor();
        rp.price = p.getPrice();
        rp.imagePaths = productImageRepo.findAllByProductId(p.getId()).stream()
                                                                        .map(ImagePathOnly::getImagePath)
                                                                        .collect(Collectors.toList());

        return rp;
    }

    public ReadCartItem stock2ReadCardItem(Stock s) {
        ReadCartItem item = new ReadCartItem();
        item.baseProductId = s.getProduct().getBaseProduct().getId();
        item.baseProductName = s.getProduct().getBaseProduct().getName();
        item.baseProductCategory = s.getProduct().getBaseProduct().getCategory();
        item.price = s.getProduct().getPrice();
        item.productId = s.getProduct().getId();
        item.color = s.getProduct().getColor();
        item.stockId = s.getId();
        item.width = s.getWidth();
        item.height = s.getHeight();
        item.size = s.getSize();

        return item;
    }

    public ReadBaseProduct baseProduct2ReadBaseProduct(BaseProduct bp) {
        ReadBaseProduct rbp = new ReadBaseProduct();
        rbp.id = bp.getId();
        rbp.category = bp.getCategory();
        rbp.name = bp.getName();

        return rbp;
    }

//    public ReadStockList stocks2ReadStockList(List<Stock> stocks) {
//        List<StockDTO> sList = new ArrayList<>();
//        for(Stock s: stocks) {
//            sList.add(stock2StockDTO(s));
//        }
//
//        ReadStockList rs = new ReadStockList();
//        rs.stocks = sList;
//
//        return rs;
//    }

//    public StockDTO stock2StockDTO(Stock stock) {
//        StockDTO s = new StockDTO();
//        s.productId = stock.getProduct().getId();
//        s.productName = stock.getProduct().getName();
//        s.productCategory = stock.getProduct().getCategory();
//        s.stockId = stock.getId();
//        s.color = stock.getColor();
//        s.price = stock.getPrice();
//        s.width = stock.getWidth();
//        s.height = stock.getHeight();
//        s.size = stock.getSize();
//        s.quantity = stock.getQuantity();
//
//        return s;
//    }
}
