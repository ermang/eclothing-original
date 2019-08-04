package com.eg.eclothing.repo;

import com.eg.eclothing.entity.ShoppingUser;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingUserRepo extends CrudRepository<ShoppingUser, Long> {
    ShoppingUser findByUsername(String name);
}
