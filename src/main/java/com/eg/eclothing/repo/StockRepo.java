package com.eg.eclothing.repo;

import com.eg.eclothing.entity.Stock;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepo extends JpaRepository<Stock, Long> {
    List<Stock> findAllByQuantityGreaterThan(Integer quantity);

    List<Stock> findAllByIdIn(List<Long> stockIds);

    @EntityGraph(attributePaths = {"product", "product.baseProduct"})
    List<Stock> findAllByProductId(long productId);
}
