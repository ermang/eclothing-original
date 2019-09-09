package com.eg.eclothing.service;

import com.eg.eclothing.dto.BasketContent;
import com.eg.eclothing.dto.CheckoutBasket;
import com.eg.eclothing.dto.ReadBuyer;
import com.eg.eclothing.entity.Stock;
import com.eg.eclothing.entity.UnregisteredBuyer;
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

    public PaymentService(StockRepo stockRepo, UnregisteredBuyerRepo unRegisteredBuyerRepo){
        this.stockRepo = stockRepo;
        this.unRegisteredBuyerRepo = unRegisteredBuyerRepo;
    }

    public String checkout(CheckoutBasket checkoutBasket, String ip) {
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

        Buyer buyer = initAndFillUnregisteredBuyer(checkoutBasket, ip);
        request.setBuyer(buyer);

        Address shippingAddress = initAndFillShippingAddress(checkoutBasket);
        request.setShippingAddress(shippingAddress);

        Address billingAddress = initAndFillBillingAddress(checkoutBasket);
        request.setBillingAddress(billingAddress);

        List<BasketItem> basketItems = new ArrayList<>();

        for(BasketContent bc : checkoutBasket.basketContents) {
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

    private Buyer initAndFillUnregisteredBuyer(CheckoutBasket checkoutBasket, String ip) {
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

        UnregisteredBuyer urb = readBuyer2UnregisteredBuyer(checkoutBasket.readBuyer, ip);
        urb = unRegisteredBuyerRepo.save(urb);

        buyer.setId(String.valueOf(urb.getId()));

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

    private Address initAndFillShippingAddress(CheckoutBasket checkoutBasket) {
        Address shippingAddress = new Address();
        shippingAddress.setContactName(checkoutBasket.shippindAddress.contactName);
        shippingAddress.setCity(checkoutBasket.shippindAddress.city);
        shippingAddress.setCountry(checkoutBasket.shippindAddress.country);
        shippingAddress.setAddress(checkoutBasket.shippindAddress.address);
        shippingAddress.setZipCode(checkoutBasket.shippindAddress.zipCode);

        return shippingAddress;
    }

    private Address initAndFillBillingAddress(CheckoutBasket checkoutBasket) {
        Address billingAddress = new Address();
        billingAddress.setContactName(checkoutBasket.shippindAddress.contactName);
        billingAddress.setCity(checkoutBasket.shippindAddress.city);
        billingAddress.setCountry(checkoutBasket.shippindAddress.country);
        billingAddress.setAddress(checkoutBasket.shippindAddress.address);
        billingAddress.setZipCode(checkoutBasket.shippindAddress.zipCode);

        return billingAddress;
    }
}
