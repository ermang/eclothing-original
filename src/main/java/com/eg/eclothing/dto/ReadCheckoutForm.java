package com.eg.eclothing.dto;

import java.math.BigDecimal;

public class ReadCheckoutForm {
    public String token;
    public String callbackUrl;
    public String status;
    public String paymentStatus;
    public String errorCode;
    public String errorGroup;
    public String locale;
    public long systemTime;
    public String conversationId;
    public String paymentId;
    public BigDecimal price;
    public BigDecimal paidPrice;
    public String currency;
    public Integer installment;
    public String basketId;
    public String binNumber;
    public String lastFourDigits;
    public String cardAssociation;
    public String cardFamily;
    public String cardType;
    public Integer fraudStatus;
    public BigDecimal iyziCommissionFee;
    public BigDecimal iyziCommissionRateAmount;
    public BigDecimal merchantCommissionRate;
    public BigDecimal merchantCommissionRateAmount;
    public String errorMessage;

    @Override
    public String toString() {
        return "ReadCheckoutForm{" +
                "token='" + token + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", status='" + status + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorGroup='" + errorGroup + '\'' +
                ", locale='" + locale + '\'' +
                ", systemTime=" + systemTime +
                ", conversationId='" + conversationId + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", price=" + price +
                ", paidPrice=" + paidPrice +
                ", currency='" + currency + '\'' +
                ", installment=" + installment +
                ", basketId='" + basketId + '\'' +
                ", binNumber='" + binNumber + '\'' +
                ", lastFourDigits='" + lastFourDigits + '\'' +
                ", cardAssociation='" + cardAssociation + '\'' +
                ", cardFamily='" + cardFamily + '\'' +
                ", cardType='" + cardType + '\'' +
                ", fraudStatus=" + fraudStatus +
                ", iyziCommissionFee=" + iyziCommissionFee +
                ", iyziCommissionRateAmount=" + iyziCommissionRateAmount +
                ", merchantCommissionRate=" + merchantCommissionRate +
                ", merchantCommissionRateAmount=" + merchantCommissionRateAmount +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
