package com.eg.eclothing.dto;

import java.util.List;

public class CheckoutBasket {
    public List<BasketContent> basketContents;
    public ReadAddress shippindAddress;
    public ReadAddress billingAddress;
    public ReadBuyer readBuyer;
}
