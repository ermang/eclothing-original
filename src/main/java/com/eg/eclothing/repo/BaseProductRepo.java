package com.eg.eclothing.repo;

import com.eg.eclothing.entity.BaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseProductRepo extends JpaRepository<BaseProduct, Long> {
}
