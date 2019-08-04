package com.eg.eclothing.service;

import com.eg.eclothing.dto.*;
import com.eg.eclothing.entity.*;
import com.eg.eclothing.packy.Role;
import com.eg.eclothing.repo.*;
import com.eg.eclothing.util.DTO2Entity;
import com.eg.eclothing.util.Entity2DTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {
    private final ImageService imageService;
    private final ShoppingUserRepo shoppingUserRepo;
    private final BaseProductRepo baseProductRepo;
    private final ProductRepo productRepo;
    private final ProductImageRepo productImageRepo;
    private final StockRepo stockRepo;
    private final DTO2Entity dto2Entity;
    private final Entity2DTO entity2DTO;

    public MainService(ImageService imageService,ShoppingUserRepo shoppingUserRepo, BaseProductRepo baseProductRepo,
                       ProductRepo productRepo, ProductImageRepo productImageRepo, StockRepo stockRepo,
                       DTO2Entity dto2Entity, Entity2DTO entity2DTO) {
        this.imageService = imageService;
        this.shoppingUserRepo = shoppingUserRepo;
        this.baseProductRepo = baseProductRepo;
        this.productRepo = productRepo;
        this.productImageRepo = productImageRepo;
        this.stockRepo = stockRepo;
        this.dto2Entity = dto2Entity;
        this.entity2DTO = entity2DTO;
    }

    public Long createUser(CreateUserDTO createUserDTO) {
        ShoppingUser shoppingUser = new ShoppingUser();
        shoppingUser.setUsername(createUserDTO.username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(createUserDTO.password);
        shoppingUser.setPassword(encodedPassword);
        shoppingUser.setEnabled(true);
        shoppingUser.setRole(Role.USER.getValue());
        shoppingUser.setCreatedOn(LocalDateTime.now());
        shoppingUser = shoppingUserRepo.save(shoppingUser);

        return shoppingUser.getId();
    }

    public Long createBaseProduct(CreateBaseProduct cbp) {
        BaseProduct bp = dto2Entity.createBaseProduct2BaseProduct(cbp);
        bp = baseProductRepo.save(bp);

        return bp.getId();
    }

    public Long createProduct(CreateProduct cp) {
        Product p = dto2Entity.createProduct2Product(cp);
        p = productRepo.save(p);

        List<String> imagePaths = imageService.generateProductImagePaths(cp, p.getId());
        List<ProductImage> productImages = new ArrayList<>();

        for(String s: imagePaths){
            ProductImage pi = new ProductImage();
            pi.setImagePath(s);
            pi.setProduct(p);
            productImages.add(pi);
        }

        productImageRepo.saveAll(productImages);

        try {
            imageService.createProductImages(cp, p.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return p.getId();
    }

    public Long createStock(CreateStock cs) {
        Stock stock=dto2Entity.createStock2Stock(cs);
        stock = stockRepo.save(stock);

        return stock.getId();
    }

    public ReadProductList readAllProducts() {
        List<Product> products = productRepo.findAll();//ProductsJoinedWithBaseProducts();//findAll();
        List<ReadProduct> readProducts = new ArrayList<>();
        for(Product p: products)
            readProducts.add(entity2DTO.product2ReadProduct(p));

        ReadProductList readProductList = new ReadProductList();
        readProductList.readProducts = readProducts;

        return readProductList;
    }

    public ReadBaseProducts readAllBaseProducts() {
        List<BaseProduct> baseProducts = baseProductRepo.findAll();
        List<ReadBaseProduct> readBaseProductList = new ArrayList<>();

        for(BaseProduct bp: baseProducts)
            readBaseProductList.add(entity2DTO.baseProduct2ReadBaseProduct(bp));

        ReadBaseProducts readBaseProducts = new ReadBaseProducts();
        readBaseProducts.readBaseProducts = readBaseProductList;

        return readBaseProducts;
    }

//    public ReadStock readStock(Long id) {
//
//    }

//    public ReadStockList readAllStock() {
//        List<Stock> stocks = stockRepo.findAll();
//        ReadStockList readStockList = entity2DTO.stocks2ReadStockList(stocks);
//
//        return readStockList;
//    }
//
//    public ReadStockList readAllStockWithQuantityGreaterThanZero() {
//        List<Stock> stocks = stockRepo.findAllByQuantityGreaterThan(0);
//        ReadStockList readStockList = entity2DTO.stocks2ReadStockList(stocks);
//
//        return readStockList;
//    }


}
