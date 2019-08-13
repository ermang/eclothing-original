package com.eg.eclothing.repo;

import com.eg.eclothing.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
//    @Query(value = "select p from Product p group by p.readBaseProduct.id")
//    List<Product> findAllProductsGroupByBaseProductId();

    //@EntityGraph(value = "Product.readBaseProduct" ,type = EntityGraph.EntityGraphType.LOAD)
    @EntityGraph(attributePaths = {"baseProduct"})
    @Query(value = "select p from Product p where"
                + " (:category IS NULL OR p.baseProduct.category = :category) AND"
                + " (:minPrice IS NULL OR p.price >= :minPrice) AND"
                + " (:maxPrice IS NULL OR p.price <= :maxPrice) AND"
                + " (:color IS NULL OR p.color = :color)")
    List<Product> findAll(@Param("category") String category, @Param("minPrice") BigDecimal minPrice,
                          @Param("maxPrice") BigDecimal maxPrice, @Param("color") String color);
}
