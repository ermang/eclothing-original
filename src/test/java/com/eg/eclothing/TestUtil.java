package com.eg.eclothing;

import com.eg.eclothing.dto.CreateBaseProduct;
import com.eg.eclothing.dto.CreateProduct;
import com.eg.eclothing.dto.CreateStock;
import com.eg.eclothing.dto.CreateUserDTO;

public class TestUtil {

    public CreateUserDTO createUserDTO() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.username = "user";
        createUserDTO.password = "password";

        return createUserDTO;
    }

    public CreateBaseProduct createBaseProduct(String category, String name) {
        CreateBaseProduct cbp = new CreateBaseProduct();
        cbp.category = category;
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
