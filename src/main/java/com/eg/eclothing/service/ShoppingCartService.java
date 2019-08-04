package com.eg.eclothing.service;

import com.eg.eclothing.dto.AddProductToCart;
import com.eg.eclothing.dto.ReadCart;
import com.eg.eclothing.dto.ReadCartItem;
import com.eg.eclothing.entity.ShoppingUser;
import com.eg.eclothing.entity.Stock;
import com.eg.eclothing.packy.ActiveUserResolver;
import com.eg.eclothing.repo.ShoppingUserRepo;
import com.eg.eclothing.repo.StockRepo;
import com.eg.eclothing.util.Entity2DTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    private final ShoppingUserRepo shoppingUserRepo;
    private final StockRepo stockRepo;
    private final Entity2DTO entity2DTO;
    private final ActiveUserResolver activeUserResolver;
    private Map<Long, List<AddProductToCart>> userProductMap;

    public ShoppingCartService(ShoppingUserRepo shoppingUserRepo, StockRepo stockRepo, Entity2DTO entity2DTO,
                               ActiveUserResolver activeUserResolver){
        this.shoppingUserRepo = shoppingUserRepo;
        this.stockRepo = stockRepo;
        this.entity2DTO = entity2DTO;
        this.activeUserResolver = activeUserResolver;
        this.userProductMap = new HashMap<>();
    }
    public boolean addItemToCart(AddProductToCart addProductToCart) {
        String username = activeUserResolver.getActiveUser().getUsername();
        ShoppingUser user = shoppingUserRepo.findByUsername(username);

        if (userProductMap.containsKey(user.getId()))
            userProductMap.get(user.getId()).add(addProductToCart);

        userProductMap.put(user.getId(), Arrays.asList(addProductToCart));

        return true;
    }

    public ReadCart readUserCart() {
        String username = activeUserResolver.getActiveUser().getUsername();
        ShoppingUser user = shoppingUserRepo.findByUsername(username);
        List<AddProductToCart> productsInCart = userProductMap.get(user.getId());

        List<Long> stockIds = productsInCart.stream().map(AddProductToCart::getStockId).collect(Collectors.toList());

        List<Stock> stocks = stockRepo.findAllByIdIn(stockIds);

        List<ReadCartItem> readCartItems = new ArrayList<>();
        ReadCart cart = new ReadCart();
        for(Stock s : stocks)
            readCartItems.add(entity2DTO.stock2ReadCardItem(s));

        cart.cartItems = readCartItems;

        return cart;
    }
}
