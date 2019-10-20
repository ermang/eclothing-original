package com.eg.eclothing.controller;

import com.eg.eclothing.dto.*;
import com.eg.eclothing.service.MainService;
import com.eg.eclothing.service.PaymentService;
import com.eg.eclothing.service.ShoppingCartService;
import com.iyzipay.Options;
import com.iyzipay.model.CheckoutForm;
import com.iyzipay.model.Locale;
import com.iyzipay.request.RetrieveCheckoutFormRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
//@CrossOrigin(value = "http://localhost:4200" )//TODO: FIXME
public class MainController {

    private final MainService mainService;
    private final PaymentService paymentService;
    private final ShoppingCartService shoppingCartService;
//    private final CreateUserDTOValidator createUserDTOValidator;
//    private final CreateTopicDTOValidator createTopicDTOValidator;
//    private final CreateThreadDTOValidator createThreadDTOValidator;

    public MainController(MainService mainService, ShoppingCartService shoppingCartService, PaymentService paymentService) {
        this.mainService = mainService;
        this.shoppingCartService =shoppingCartService;
        this.paymentService = paymentService;

    }

    @RequestMapping("/")
    public String greeting() {
        return "Welcome to Ecommerce";
    }

    @PostMapping("/login")
    public boolean login() {
        return true;
    }

    @PostMapping("/category")
    public Long createCategory(@RequestBody CreateCategory createCategory) {
        Long result = mainService.createCategory(createCategory);

        return result;
    }

    @PostMapping("/baseProduct")
    public Long createBaseProduct(@RequestBody CreateBaseProduct createBaseProduct) {
        Long result = mainService.createBaseProduct(createBaseProduct);
        return result;
    }

    @PostMapping("/product")
    public Long createProduct(@RequestBody CreateProduct createProduct) {
        Long result = mainService.createProduct(createProduct);
        return result;
    }

    @PostMapping("/stock")
    public Long createStock(@RequestBody CreateStock createStock) {
        Long result = mainService.createStock(createStock);

        return result;
    }

    @GetMapping("/baseProducts")
    public ReadBaseProducts readBaseProductList(){
        ReadBaseProducts result = mainService.readAllBaseProducts();

        return result;
    }

    @GetMapping("/categories")
    public ReadCategories readCategoryList(){
        ReadCategories result = mainService.readAllCategories();

        return result;
    }

    @GetMapping("/products")
    public ReadProductList readProductList(@RequestParam(required = false)  String category,
                                           @RequestParam(required = false) BigDecimal minPrice,
                                           @RequestParam(required = false)  BigDecimal maxPrice,
                                           @RequestParam(required = false)  String color){
        ReadProductList result = mainService.readAllProducts(category, minPrice, maxPrice, color);
        return result;
    }

    @GetMapping("/product/{productId}/stock")
    public ReadProductStock readProductStock(@PathVariable long productId) {
        ReadProductStock result = mainService.readProductStock(productId);

        return result;
    }

    @PostMapping("/cart")
    public boolean addItemToCart(@RequestBody AddProductToCart addProductToCart) {
        boolean result = shoppingCartService.addItemToCart(addProductToCart);

        return result;
    }

    @GetMapping("/cart")
    public ReadCart readCart() {
        ReadCart result = shoppingCartService.readUserCart();

        return result;
    }

    @PostMapping("/user")
    public Long createUser(@RequestBody CreateUserDTO createUserDTO) {
        Long result = mainService.createUser(createUserDTO);

        return result;
    }

    @PostMapping("/checkout")
    public ReadCheckoutFormInitialize checkout(@RequestBody CheckoutBasket checkoutBasket, HttpServletRequest request) {
        ReadCheckoutFormInitialize result = paymentService.checkout(checkoutBasket, request.getRemoteAddr());

        return result;
    }

    @PostMapping("/payment-result")
    public String paymentResult(@RequestBody String token, HttpServletRequest request) {
        String tokenTrimmed = token.split("=")[1];

        Options options = new Options();
        options.setApiKey("sandbox-etzdcNHoKGk6f3sWOyss7PGIWMbJLvMi");
        options.setSecretKey("sandbox-ocT5lGkZaPrU416iKnmbRf5wOtEex64M");
        options.setBaseUrl("https://sandbox-api.iyzipay.com");//(System.getProperty("baseUrl"));

        RetrieveCheckoutFormRequest request2 = new RetrieveCheckoutFormRequest();
        request2.setLocale(Locale.TR.getValue());
        request2.setToken(tokenTrimmed);

        CheckoutForm checkoutForm = CheckoutForm.retrieve(request2, options);

        ReadCheckoutForm result = new ReadCheckoutForm();
        result.token = checkoutForm.getToken();
        result.callbackUrl = checkoutForm.getCallbackUrl();
        result.status = checkoutForm.getStatus();
        result.paymentStatus = checkoutForm.getPaymentStatus();
        result.errorCode = checkoutForm.getErrorCode();
        result.errorGroup = checkoutForm.getErrorGroup();
        result.errorMessage = checkoutForm.getErrorMessage();
        result.locale = checkoutForm.getLocale();
        result.systemTime = checkoutForm.getSystemTime();
        result.conversationId = checkoutForm.getConversationId();
        result.paymentId = checkoutForm.getPaymentId();
        result.price = checkoutForm.getPrice();
        result.paidPrice = checkoutForm.getPaidPrice();
        result.currency = checkoutForm.getCurrency();
        result.installment = checkoutForm.getInstallment();
        result.basketId = checkoutForm.getBasketId();
        result.binNumber = checkoutForm.getBinNumber();
        result.lastFourDigits = checkoutForm.getLastFourDigits();
        result.cardAssociation = checkoutForm.getCardAssociation();
        result.cardFamily = checkoutForm.getCardFamily();
        result.cardType = checkoutForm.getCardType();
        result.fraudStatus = checkoutForm.getFraudStatus();
        result.iyziCommissionFee = checkoutForm.getIyziCommissionFee();
        result.iyziCommissionRateAmount = checkoutForm.getIyziCommissionRateAmount();
        result.merchantCommissionRate = checkoutForm.getMerchantCommissionRate();
        result.merchantCommissionRateAmount = checkoutForm.getIyziCommissionRateAmount();

        return "<div>hoppa gecmis olsun" + result.toString() + "</div>";
    }

}
