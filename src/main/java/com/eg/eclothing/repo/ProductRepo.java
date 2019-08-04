package com.eg.eclothing.repo;

import com.eg.eclothing.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
//    @Query(value = "select p from Product p group by p.baseProduct.id")
//    List<Product> findAllProductsGroupByBaseProductId();

    //@EntityGraph(value = "Product.baseProduct" ,type = EntityGraph.EntityGraphType.LOAD)
    @EntityGraph(attributePaths = {"baseProduct"})
    //@Query(value = "select p from Product")
    List<Product> findAll();//ProductsJoinedWithBaseProducts();
}
