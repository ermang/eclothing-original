package com.eg.eclothing.service;

import com.eg.eclothing.dto.*;
import com.eg.eclothing.entity.*;
import com.eg.eclothing.packy.Role;
import com.eg.eclothing.repo.*;
import com.eg.eclothing.repo.projection.CategoryOnly;
import com.eg.eclothing.util.DTO2Entity;
import com.eg.eclothing.util.Entity2DTO;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {
    private final ImageService imageService;
    private final ShoppingUserRepo shoppingUserRepo;
    private final CategoryRepo categoryRepo;
    private final BaseProductRepo baseProductRepo;
    private final ProductRepo productRepo;
    private final ProductImageRepo productImageRepo;
    private final StockRepo stockRepo;
    private final DTO2Entity dto2Entity;
    private final Entity2DTO entity2DTO;

    public MainService(ImageService imageService,ShoppingUserRepo shoppingUserRepo, CategoryRepo categoryRepo,
                       BaseProductRepo baseProductRepo, ProductRepo productRepo, ProductImageRepo productImageRepo,
                       StockRepo stockRepo, DTO2Entity dto2Entity, Entity2DTO entity2DTO) {
        this.imageService = imageService;
        this.shoppingUserRepo = shoppingUserRepo;
        this.categoryRepo = categoryRepo;
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

    public ReadProductList readAllProducts(String category, BigDecimal minPrice, BigDecimal maxPrice, String color) {
        List<Product> products = productRepo.findAll(category, minPrice, maxPrice, color);//ProductsJoinedWithBaseProducts();//findAll();
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

    public ReadProductStock readProductStock(long productId) {
        List<Stock> stocks = stockRepo.findAllByProductId(productId);
        ReadProductStock rps = entity2DTO.stocks2ReadProductStock(stocks);

        return rps;
    }

    public Boolean buyItems(AddProductToCartList addProductToCartList) {
        for(AddProductToCart item: addProductToCartList.cartItems) {
            Stock s =stockRepo.findById(item.stockId).get();
            s.setQuantity(s.getQuantity() - item.quantity);

            s = stockRepo.save(s);
        }

        return true;
    }

    public ReadCategories readAllCategories() {
        List<Category> categoryList = categoryRepo.findAll();

        List<ReadCategory> categories = new ArrayList<>();
        for(Category c: categoryList)
            categories.add(entity2DTO.category2ReadCategory(c));

        ReadCategories rc = new ReadCategories();
        rc.categories = categories;
        return rc;
    }

    public Long createCategory(CreateCategory createCategory) {
        Category c = new Category();
        c.setName(createCategory.name);

        c = categoryRepo.save(c);

        return c.getId();
    }

    public String checkout(ReadBasket readBasket) {
        Options options = new Options();
        options.setApiKey("sandbox-etzdcNHoKGk6f3sWOyss7PGIWMbJLvMi");
        options.setSecretKey("sandbox-ocT5lGkZaPrU416iKnmbRf5wOtEex64M");
        options.setBaseUrl("https://sandbox-api.iyzipay.com");//(System.getProperty("baseUrl"));

        CreateCheckoutFormInitializeRequest request = new CreateCheckoutFormInitializeRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId("123456789");
        request.setPrice(new BigDecimal("99.99"));
        request.setPaidPrice(new BigDecimal("99.99"));
        request.setCurrency(Currency.TRY.name());
        request.setBasketId("B67832");
        request.setPaymentGroup(PaymentGroup.PRODUCT.name());
        request.setCallbackUrl("https://www.merchant.com/callback");
        request.setDebitCardAllowed(Boolean.TRUE);

        List<Integer> enabledInstallments = new ArrayList<>();
        enabledInstallments.add(2);
        enabledInstallments.add(3);
        enabledInstallments.add(6);
        enabledInstallments.add(9);
        request.setEnabledInstallments(enabledInstallments);

        Buyer buyer = new Buyer();
        buyer.setId("BY789");
        buyer.setName("John");
        buyer.setSurname("Doe");
        buyer.setGsmNumber("+905350000000");
        buyer.setEmail("email@email.com");
        buyer.setIdentityNumber("74300864791");
        buyer.setLastLoginDate("2015-10-05 12:43:35");
        buyer.setRegistrationDate("2013-04-21 15:12:09");
        buyer.setRegistrationAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        buyer.setIp("85.34.78.112");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setZipCode("34732");
        request.setBuyer(buyer);

        Address shippingAddress = new Address();
        shippingAddress.setContactName("Jane Doe");
        shippingAddress.setCity("Istanbul");
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        shippingAddress.setZipCode("34742");
        request.setShippingAddress(shippingAddress);

        Address billingAddress = new Address();
        billingAddress.setContactName("Jane Doe");
        billingAddress.setCity("Istanbul");
        billingAddress.setCountry("Turkey");
        billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        billingAddress.setZipCode("34742");
        request.setBillingAddress(billingAddress);

        List<BasketItem> basketItems = new ArrayList<BasketItem>();

        for(BasketContent bc : readBasket.basketContents) {
            BasketItem basketItem = new BasketItem();

            Stock s = stockRepo.findById(bc.stockId).get();
            basketItem.setId(s.getId().toString());
            basketItem.setName(s.getProduct().getBaseProduct().getName());
            basketItem.setCategory1(s.getProduct().getBaseProduct().getCategory().getName());
            basketItem.setItemType(BasketItemType.PHYSICAL.name());
            basketItem.setPrice(s.getProduct().getPrice());

            basketItems.add(basketItem);
        }

        request.setBasketItems(basketItems);

        CheckoutFormInitialize checkoutFormInitialize = CheckoutFormInitialize.create(request, options);

        return checkoutFormInitialize.getPaymentPageUrl();
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
