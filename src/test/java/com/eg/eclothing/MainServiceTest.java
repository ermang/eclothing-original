package com.eg.eclothing;

import com.eg.eclothing.dto.*;
import com.eg.eclothing.entity.ShoppingUser;
import com.eg.eclothing.packy.ActiveUserResolver;
import com.eg.eclothing.repo.*;
import com.eg.eclothing.service.ImageService;
import com.eg.eclothing.service.MainService;
import com.eg.eclothing.service.ShoppingCartService;
import com.eg.eclothing.util.DTO2Entity;
import com.eg.eclothing.util.Entity2DTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
//@WithMockUser   //DefaultUser with username "user", password "password", and a single GrantedAuthority named "ROLE_USER"
@DataJpaTest(includeFilters = @ComponentScan.Filter(classes = {Service.class}))
public class MainServiceTest {

    @Autowired
    private ShoppingUserRepo shoppingUserRepo;
    @Autowired
    private BaseProductRepo baseProductRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductImageRepo productImageRepo;
    @Autowired
    private StockRepo stockRepo;
    @Autowired
    private DTO2Entity dto2Entity;
    @Autowired
    private Entity2DTO entity2DTO;

    private MainService mainService;
    private ShoppingCartService shoppingCartService;

    private ActiveUserResolver activeUserResolver;
    private UserDetails userDetails;
    private TestUtil testUtil;


    @Before
    public void setup() {
        testUtil = new TestUtil();
        activeUserResolver = Mockito.mock(ActiveUserResolver.class);
        userDetails = Mockito.mock(UserDetails.class);
        mainService = new MainService(Mockito.mock(ImageService.class), shoppingUserRepo, baseProductRepo, productRepo,
                productImageRepo, stockRepo, dto2Entity, entity2DTO);

        shoppingCartService = new ShoppingCartService(shoppingUserRepo, stockRepo, entity2DTO, activeUserResolver);
    }

    @Test
    public void testy() {
        Assert.assertEquals(17, 17);
    }

    @Test
    public void createBaseProduct() {
        CreateBaseProduct cbp = testUtil.createBaseProduct("chino pantolon", "pantolon");

        Long result = mainService.createBaseProduct(cbp);
        Assert.assertNotNull(result);
    }

    @Test
    public void createProduct() {
        CreateBaseProduct cbp = testUtil.createBaseProduct("chino pantolon", "pantolon");
        Long baseProductId = mainService.createBaseProduct(cbp);
        CreateProduct cp = testUtil.createProduct(baseProductId);

        Long result = mainService.createProduct(cp);

        Assert.assertNotNull(result);
    }

    @Test
    public void createStock() {
        CreateBaseProduct cbp = testUtil.createBaseProduct("chino pantolon", "pantolon");
        Long baseProductId = mainService.createBaseProduct(cbp);
        CreateProduct cp = testUtil.createProduct(baseProductId);
        Long productId = mainService.createProduct(cp);
        CreateStock cs = testUtil.createStock(productId);

        Long result = mainService.createStock(cs);

        Assert.assertNotNull(result);
    }

    @Test
    public void readAllProducts() {
        CreateBaseProduct cbp = testUtil.createBaseProduct("chino pantolon", "pantolon");
        Long baseProductId = mainService.createBaseProduct(cbp);
        CreateProduct cp = testUtil.createProduct(baseProductId);
        mainService.createProduct(cp);
        mainService.createProduct(cp);
        mainService.createProduct(cp);

        ReadProductList result = mainService.readAllProducts(null, null, null);
        Assert.assertEquals(3, result.readProducts.size());
    }

    @Test
    public void addItemToCart() {
        mainService.createUser(testUtil.createUserDTO());
        Mockito.when(userDetails.getUsername()).thenReturn("user");

        Mockito.when(activeUserResolver.getActiveUser()).thenReturn(userDetails);
        AddProductToCart apc = new AddProductToCart();
        shoppingCartService.addItemToCart(apc);
    }

    @Test
    public void readUserCart() {
        CreateBaseProduct cbp = testUtil.createBaseProduct("chino pantolon", "pantolon");
        Long baseProductId = mainService.createBaseProduct(cbp);
        CreateProduct cp = testUtil.createProduct(baseProductId);
        Long productId = mainService.createProduct(cp);
        CreateStock cs = testUtil.createStock(productId, "M", 50);
        Long stockId = mainService.createStock(cs);

        mainService.createUser(testUtil.createUserDTO());
        Mockito.when(userDetails.getUsername()).thenReturn("user");

        Mockito.when(activeUserResolver.getActiveUser()).thenReturn(userDetails);
        AddProductToCart apc = new AddProductToCart();
        apc.productId = productId;
        apc.stockId = stockId;

        shoppingCartService.addItemToCart(apc);
        ReadCart result = shoppingCartService.readUserCart();

        Assert.assertEquals(1, result.cartItems.size());
    }

//
//    @Test
//    public void createProductAndStock() {
//        CreateProduct cp = new CreateProduct();
//        cp.name = "chino pantolon";
//        cp.category = "pantolon";
//        Long productId = mainService.createProduct(cp);
//
//        CreateStock cs = new CreateStock();
//        cs.productId = productId;
//        cs.color = "red";
//        Long result = mainService.createStock(cs);
//        Assert.assertNotNull(result);
//    }
//
//    @Test
//    public void readAllStock() {
//        CreateProduct cp = new CreateProduct();
//        cp.name = "chino pantolon";
//        cp.category = "pantolon";
//        Long productId = mainService.createProduct(cp);
//
//        CreateStock cs = new CreateStock();
//        cs.productId = productId;
//        cs.color = "red";
//        mainService.createStock(cs);
//        mainService.createStock(cs);
//
//        Assert.assertEquals(2, mainService.readAllStock().stocks.size());
//    }
//
//    @Test
//    public void readAllStockWithQuantityGreaterThanZero() {
//        CreateProduct cp = new CreateProduct();
//        cp.name = "chino pantolon";
//        cp.category = "pantolon";
//        Long productId = mainService.createProduct(cp);
//
//        CreateStock cs = new CreateStock();
//        cs.color = "red";
//        cs.productId = productId;
//        cs.quantity = 17;
//        mainService.createStock(cs);
//        mainService.createStock(cs);
//
//        Assert.assertEquals(2, mainService.readAllStockWithQuantityGreaterThanZero().stocks.size());
//    }
}
