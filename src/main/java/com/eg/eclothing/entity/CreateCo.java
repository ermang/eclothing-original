package com.eg.eclothing.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class CreateCo extends BaseEntity{
     private BigDecimal price;
     private BigDecimal paidPrice;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "unregistered_buyer_id")
    private UnregisteredBuyer unregisteredBuyer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "contactName", column = @Column(name = "ship_contact_name")),
            @AttributeOverride( name = "city", column = @Column(name = "ship_city")),
            @AttributeOverride( name = "country", column = @Column(name = "ship_phone")),
            @AttributeOverride( name = "address", column = @Column(name = "ship_address")),
            @AttributeOverride( name = "zipCode", column = @Column(name = "ship_zip_code"))
    })
    private CoAddress shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "contactName", column = @Column(name = "bill_contact_name")),
            @AttributeOverride( name = "city", column = @Column(name = "bill_city")),
            @AttributeOverride( name = "country", column = @Column(name = "bill_phone")),
            @AttributeOverride( name = "address", column = @Column(name = "bill_address")),
            @AttributeOverride( name = "zipCode", column = @Column(name = "bill_zip_code"))
    })
    private CoAddress billingAddress;

    private String status;
    private String errorCode;
    private String errorMessage;
    private String errorGroup;
    private Long systemTime;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(BigDecimal paidPrice) {
        this.paidPrice = paidPrice;
    }

    public UnregisteredBuyer getUnregisteredBuyer() {
        return unregisteredBuyer;
    }

    public void setUnregisteredBuyer(UnregisteredBuyer unregisteredBuyer) {
        this.unregisteredBuyer = unregisteredBuyer;
    }


    public CoAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(CoAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public CoAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(CoAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorGroup() {
        return errorGroup;
    }

    public void setErrorGroup(String errorGroup) {
        this.errorGroup = errorGroup;
    }

    public Long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(Long systemTime) {
        this.systemTime = systemTime;
    }
}
