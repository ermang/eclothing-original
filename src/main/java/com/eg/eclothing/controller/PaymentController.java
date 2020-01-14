package com.eg.eclothing.controller;

import com.eg.eclothing.dto.CheckoutBasket;
import com.eg.eclothing.dto.ReadCheckoutForm;
import com.eg.eclothing.dto.ReadCheckoutFormInitialize;
import com.eg.eclothing.service.PaymentService;
import com.iyzipay.Options;
import com.iyzipay.model.CheckoutForm;
import com.iyzipay.model.Locale;
import com.iyzipay.request.RetrieveCheckoutFormRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
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
