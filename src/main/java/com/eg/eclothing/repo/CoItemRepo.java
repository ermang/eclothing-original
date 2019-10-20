package com.eg.eclothing.repo;

import com.eg.eclothing.entity.CoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoItemRepo extends JpaRepository<CoItem, Long> {
}
