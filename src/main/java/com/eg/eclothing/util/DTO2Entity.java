package com.eg.eclothing.util;

import com.eg.eclothing.dto.CreateBaseProduct;
import com.eg.eclothing.dto.CreateProduct;
import com.eg.eclothing.dto.CreateProductWithStock;
import com.eg.eclothing.dto.CreateStock;
import com.eg.eclothing.entity.BaseProduct;
import com.eg.eclothing.entity.Category;
import com.eg.eclothing.entity.Product;
import com.eg.eclothing.entity.Stock;
import com.eg.eclothing.repo.BaseProductRepo;
import com.eg.eclothing.repo.CategoryRepo;
import com.eg.eclothing.repo.ProductRepo;
import org.springframework.stereotype.Service;

@Service
public class DTO2Entity {
    private final CategoryRepo categoryRepo;
    private final BaseProductRepo baseProductRepo;
    private final ProductRepo productRepo;

    public DTO2Entity(CategoryRepo categoryRepo, BaseProductRepo baseProductRepo, ProductRepo productRepo) {
        this.categoryRepo = categoryRepo;
        this.baseProductRepo = baseProductRepo;
        this.productRepo = productRepo;
    }

    public Product createProduct2Product(CreateProduct cp) {
        Product p = new Product();
        p.setBaseProduct(baseProductRepo.findById(cp.baseProductId).get());
        p.setColor(cp.color);
        p.setPrice(cp.price);

        return p;
    }
//    public Stock createProductWithStock2Stock(CreateProductWithStock c,Product product) {
//        Stock stock=new Stock();
//        stock.setProduct(product);
//        stock.setColor(c.color);
//        stock.setSize(c.size);
//        stock.setHeight(c.height);
//        stock.setWidth(c.width);
//        stock.setPrice(c.price);
//        stock.setQuantity(c.quantity);
//        return stock;
//    }
//    public Product createProductWithStock2Product(CreateProductWithStock c) {
//        Product product=new Product();
//        product.setName(c.name);
//        product.setCategory(c.name);
//
//        return product;
//    }
    public Stock createStock2Stock(CreateStock cs){
        Stock stock=new Stock();
        Product p = productRepo.findById(cs.productId).get();
        stock.setProduct(p);
        stock.setHeight(cs.height);
        stock.setWidth(cs.width);
        stock.setSize(cs.size);
        stock.setQuantity(cs.quantity);

        return stock;
    }

    public BaseProduct createBaseProduct2BaseProduct(CreateBaseProduct cbp) {
        BaseProduct bp =  new BaseProduct();
        Category c=  categoryRepo.findById(cbp.categoryId).get();
        bp.setCategory(c);
        bp.setName(cbp.name);

        return bp;
    }
}
