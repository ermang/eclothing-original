package com.eg.eclothing;

import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;

import com.iyzipay.request.RetrieveCheckoutFormRequest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IyzicoTest {


    @Test
    public void testIyzico() {
        Options options = new Options();
        options.setApiKey("sandbox-etzdcNHoKGk6f3sWOyss7PGIWMbJLvMi");
        options.setSecretKey("sandbox-ocT5lGkZaPrU416iKnmbRf5wOtEex64M");
        options.setBaseUrl("https://sandbox-api.iyzipay.com");//(System.getProperty("baseUrl"));

        CreateCheckoutFormInitializeRequest request = new CreateCheckoutFormInitializeRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId("123456789");
        request.setPrice(new BigDecimal("1"));
        request.setPaidPrice(new BigDecimal("1.2"));
        request.setCurrency(Currency.TRY.name());
        request.setBasketId("B67832");
        request.setPaymentGroup(PaymentGroup.PRODUCT.name());
        request.setCallbackUrl("https://www.merchant.com/callback");
        request.setDebitCardAllowed(Boolean.TRUE);

        List<Integer> enabledInstallments = new ArrayList<Integer>();
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
        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId("BI101");
        firstBasketItem.setName("Binocular");
        firstBasketItem.setCategory1("Collectibles");
        firstBasketItem.setCategory2("Accessories");
        firstBasketItem.setItemType(BasketItemType.PHYSICAL.name());
        firstBasketItem.setPrice(new BigDecimal("0.3"));
        basketItems.add(firstBasketItem);

        BasketItem secondBasketItem = new BasketItem();
        secondBasketItem.setId("BI102");
        secondBasketItem.setName("Game code");
        secondBasketItem.setCategory1("Game");
        secondBasketItem.setCategory2("Online Game Items");
        secondBasketItem.setItemType(BasketItemType.VIRTUAL.name());
        secondBasketItem.setPrice(new BigDecimal("0.5"));
        basketItems.add(secondBasketItem);

        BasketItem thirdBasketItem = new BasketItem();
        thirdBasketItem.setId("BI103");
        thirdBasketItem.setName("Usb");
        thirdBasketItem.setCategory1("Electronics");
        thirdBasketItem.setCategory2("Usb / Cable");
        thirdBasketItem.setItemType(BasketItemType.PHYSICAL.name());
        thirdBasketItem.setPrice(new BigDecimal("0.2"));
        basketItems.add(thirdBasketItem);
        request.setBasketItems(basketItems);

        CheckoutFormInitialize checkoutFormInitialize = CheckoutFormInitialize.create(request, options);

        System.out.println(checkoutFormInitialize);

        assertEquals(Status.SUCCESS.getValue(), checkoutFormInitialize.getStatus());
        assertEquals(Locale.TR.getValue(), checkoutFormInitialize.getLocale());
        assertEquals("123456789", checkoutFormInitialize.getConversationId());
        assertNotNull(checkoutFormInitialize.getSystemTime());
        assertNull(checkoutFormInitialize.getErrorCode());
        assertNull(checkoutFormInitialize.getErrorMessage());
        assertNull(checkoutFormInitialize.getErrorGroup());
        assertNotNull(checkoutFormInitialize.getCheckoutFormContent());
        assertNotNull(checkoutFormInitialize.getPaymentPageUrl());

        RetrieveCheckoutFormRequest request2 = new RetrieveCheckoutFormRequest();
        request2.setLocale(Locale.TR.getValue());
        request2.setConversationId("123456789");
        request2.setToken(checkoutFormInitialize.getToken());

        CheckoutForm checkoutForm = CheckoutForm.retrieve(request2, options);

        System.out.println(checkoutForm);
    }



}
