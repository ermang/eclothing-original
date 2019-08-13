package com.eg.eclothing.repo;

import com.eg.eclothing.entity.BaseProduct;
import com.eg.eclothing.repo.projection.CategoryOnly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseProductRepo extends JpaRepository<BaseProduct, Long> {

    List<CategoryOnly> findAllBy();
}
