package com.eg.eclothing.service;

import com.eg.eclothing.dto.BasketContent;
import com.eg.eclothing.dto.CheckoutBasket;
import com.eg.eclothing.dto.ReadBuyer;
import com.eg.eclothing.dto.ReadCheckoutFormInitialize;
import com.eg.eclothing.entity.*;
import com.eg.eclothing.repo.CoItemRepo;
import com.eg.eclothing.repo.CreateCoRepo;
import com.eg.eclothing.repo.StockRepo;
import com.eg.eclothing.repo.UnregisteredBuyerRepo;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    private final StockRepo stockRepo;
    private final UnregisteredBuyerRepo unRegisteredBuyerRepo;
    private final CreateCoRepo createCoRepo;
    private final CoItemRepo coItemRepo;

    public PaymentService(StockRepo stockRepo, UnregisteredBuyerRepo unRegisteredBuyerRepo, CreateCoRepo createCoRepo,
                          CoItemRepo coItemRepo){
        this.stockRepo = stockRepo;
        this.unRegisteredBuyerRepo = unRegisteredBuyerRepo;
        this.createCoRepo = createCoRepo;
        this.coItemRepo = coItemRepo;
    }

    public ReadCheckoutFormInitialize checkout(CheckoutBasket checkoutBasket, String ip) {
        Options options = initAndFillOptions();

        CreateCheckoutFormInitializeRequest request = new CreateCheckoutFormInitializeRequest();
        request.setLocale(Locale.TR.getValue());
        request.setPrice(new BigDecimal("99.99"));
        request.setPaidPrice(new BigDecimal("99.99"));
        request.setCurrency(Currency.TRY.name());
        request.setPaymentGroup(PaymentGroup.PRODUCT.name());
        request.setCallbackUrl("http://localhost:8080/payment-result");
        request.setDebitCardAllowed(Boolean.TRUE);

        initAndFillEnabledInstallments(request);

        UnregisteredBuyer urb = readBuyer2UnregisteredBuyer(checkoutBasket.readBuyer, ip);
        urb = unRegisteredBuyerRepo.save(urb);
        Buyer buyer = initAndFillBuyer(checkoutBasket, ip, urb.getId());
        request.setBuyer(buyer);

        Address shippingAddress = initAndFillShippingAddress(checkoutBasket);
        request.setShippingAddress(shippingAddress);

        Address billingAddress = initAndFillBillingAddress(checkoutBasket);
        request.setBillingAddress(billingAddress);

        CreateCo createCo = new CreateCo();
        createCo.setUnregisteredBuyer(urb);
        createCo.setBillingAddress(address2CoAddress(billingAddress));
        createCo.setShippingAddress(address2CoAddress(shippingAddress));
        createCo.setPrice(BigDecimal.ZERO);
        createCo.setPaidPrice(BigDecimal.ZERO);

        createCo = createCoRepo.save(createCo);

        List<BasketItem> basketItems = setBasketItems(checkoutBasket.basketContents);

        List<CoItem> coItems =  initAndFillCoItems(checkoutBasket.basketContents, createCo);

        coItemRepo.saveAll(coItems);

        request.setConversationId(String.valueOf(createCo.getId()));

        request.setBasketItems(basketItems);

        CheckoutFormInitialize checkoutFormInitialize = CheckoutFormInitialize.create(request, options);

        createCo.setStatus(checkoutFormInitialize.getStatus());
        createCo.setErrorCode(checkoutFormInitialize.getErrorCode());
        createCo.setErrorMessage(checkoutFormInitialize.getErrorMessage());
        createCo.setErrorGroup(checkoutFormInitialize.getErrorGroup());
        createCo.setSystemTime(checkoutFormInitialize.getSystemTime());
        createCo = createCoRepo.save(createCo);

        ReadCheckoutFormInitialize rcfi = initAndFillReadCheckoutFormInitialize(checkoutFormInitialize);
        return rcfi;
    }

    private List<BasketItem> setBasketItems(List<BasketContent> basketContents) {
        List<BasketItem> basketItems = new ArrayList<>();

        for(BasketContent bc : basketContents) {
            BasketItem basketItem = new BasketItem();

            Stock s = stockRepo.findById(bc.stockId).get();
            basketItem.setId(s.getId().toString());
            basketItem.setName(s.getProduct().getBaseProduct().getName());
            basketItem.setCategory1(s.getProduct().getBaseProduct().getCategory().getName());
            basketItem.setItemType(BasketItemType.PHYSICAL.name());
            basketItem.setPrice(s.getProduct().getPrice());

            basketItems.add(basketItem);
        }

        return basketItems;
    }

    private List<CoItem> initAndFillCoItems(List<BasketContent> basketContents, CreateCo createCo) {
        List<CoItem> coItems = new ArrayList<>();
        for(BasketContent bc : basketContents) {
            CoItem coItem = new CoItem();

            coItem.setCreateCo(createCo);
            coItem.setStock(stockRepo.findById(bc.stockId).get());
            coItems.add(coItem);
        }

        return coItems;
    }

    private void initAndFillEnabledInstallments(CreateCheckoutFormInitializeRequest request) {
        List<Integer> enabledInstallments = new ArrayList<>();
        enabledInstallments.add(2);
        enabledInstallments.add(3);
        enabledInstallments.add(6);
        enabledInstallments.add(9);
        request.setEnabledInstallments(enabledInstallments);
    }

    private Options initAndFillOptions() {
        Options options = new Options();
        options.setApiKey("sandbox-etzdcNHoKGk6f3sWOyss7PGIWMbJLvMi");
        options.setSecretKey("sandbox-ocT5lGkZaPrU416iKnmbRf5wOtEex64M");
        options.setBaseUrl("https://sandbox-api.iyzipay.com");//(System.getProperty("baseUrl"));
        return options;
    }

    private ReadCheckoutFormInitialize initAndFillReadCheckoutFormInitialize(CheckoutFormInitialize cfi) {
        ReadCheckoutFormInitialize rcfi = new ReadCheckoutFormInitialize();
        rcfi.token = cfi.getToken();
        rcfi.tokenExpireTime = cfi.getTokenExpireTime();
        rcfi.paymentPageUrl = cfi.getPaymentPageUrl();
        rcfi.status = cfi.getStatus();
        rcfi.errorCode = cfi.getErrorCode();
        rcfi.errorMessage = cfi.getErrorMessage();
        rcfi.errorGroup = cfi.getErrorGroup();
        rcfi.systemTime = cfi.getSystemTime();
        rcfi.conversationId = cfi.getConversationId();

        return rcfi;
    }

    private Buyer initAndFillBuyer(CheckoutBasket checkoutBasket, String ip, Long unregisteredBuyerId) {
        Buyer buyer = new Buyer();
        buyer.setName(checkoutBasket.readBuyer.name);
        buyer.setSurname(checkoutBasket.readBuyer.surname);
        buyer.setGsmNumber(checkoutBasket.readBuyer.gsmNumber);
        buyer.setEmail(checkoutBasket.readBuyer.email);
        buyer.setIdentityNumber(checkoutBasket.readBuyer.identityNumber);
        buyer.setRegistrationAddress(checkoutBasket.readBuyer.registrationAddress);
        buyer.setIp(ip);
        buyer.setCity(checkoutBasket.readBuyer.city);
        buyer.setCountry(checkoutBasket.readBuyer.country);

        buyer.setId(String.valueOf(unregisteredBuyerId));

        return buyer;
    }

    private UnregisteredBuyer readBuyer2UnregisteredBuyer(ReadBuyer rb, String ip) {
        UnregisteredBuyer urb = new UnregisteredBuyer();
        urb.setName(rb.name);
        urb.setSurname(rb.surname);
        urb.setGsmNumber(rb.gsmNumber);
        urb.setEmail(rb.email);
        urb.setIdentityNumber(rb.identityNumber);
        urb.setRegistrationAddress(rb.registrationAddress);
        urb.setIp(ip);
        urb.setCity(rb.city);
        urb.setCountry(rb.country);

        return urb;
    }

    private CoAddress address2CoAddress(Address a) {
        CoAddress coAddress = new CoAddress();
        coAddress.setAddress(a.getAddress());
        coAddress.setCity(a.getCity());
        coAddress.setContactName(a.getContactName());
        coAddress.setCountry(a.getCountry());
        coAddress.setZipCode(a.getZipCode());

        return coAddress;
    }

    private Address initAndFillShippingAddress(CheckoutBasket checkoutBasket) {
        Address shippingAddress = new Address();
        shippingAddress.setContactName(checkoutBasket.shippingAddress.contactName);
        shippingAddress.setCity(checkoutBasket.shippingAddress.city);
        shippingAddress.setCountry(checkoutBasket.shippingAddress.country);
        shippingAddress.setAddress(checkoutBasket.shippingAddress.address);
        shippingAddress.setZipCode(checkoutBasket.shippingAddress.zipCode);

        return shippingAddress;
    }

    private Address initAndFillBillingAddress(CheckoutBasket checkoutBasket) {
        Address billingAddress = new Address();
        billingAddress.setContactName(checkoutBasket.shippingAddress.contactName);
        billingAddress.setCity(checkoutBasket.shippingAddress.city);
        billingAddress.setCountry(checkoutBasket.shippingAddress.country);
        billingAddress.setAddress(checkoutBasket.shippingAddress.address);
        billingAddress.setZipCode(checkoutBasket.shippingAddress.zipCode);

        return billingAddress;
    }
}
