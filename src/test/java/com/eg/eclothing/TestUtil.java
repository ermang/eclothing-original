package com.eg.eclothing;

import com.eg.eclothing.dto.*;

public class TestUtil {

    public CreateUserDTO createUserDTO() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.username = "user";
        createUserDTO.password = "password";

        return createUserDTO;
    }

    public CreateCategory createCategory() {
        CreateCategory cc = new CreateCategory();
        cc.name = "pantolon";
        return cc;
    }

    public CreateBaseProduct createBaseProduct(Long categoryId, String name) {
        CreateBaseProduct cbp = new CreateBaseProduct();
        cbp.categoryId = categoryId;
        cbp.name = name;

        return cbp;
    }

    public CreateProduct createProduct(Long baseProductId) {
        CreateProduct cp = new CreateProduct();
        cp.baseProductId = baseProductId;

        return cp;
    }

    public CreateStock createStock(Long productId) {
        CreateStock cs = new CreateStock();
        cs.productId = productId;

        return cs;
    }

    public CreateStock createStock(Long productId, String size, int quantity) {
        CreateStock cs = new CreateStock();
        cs.productId = productId;
        cs.size = size;
        cs.quantity = quantity;

        return cs;
    }
}
