package com.eg.eclothing.repo;

import com.eg.eclothing.entity.ProductImage;
import com.eg.eclothing.repo.projection.ImagePathOnly;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductImageRepo extends CrudRepository<ProductImage, Long> {

    //List<ProductImage> findAllByProductId(Long id);
    List<ImagePathOnly> findAllByProductId(Long id);
}
