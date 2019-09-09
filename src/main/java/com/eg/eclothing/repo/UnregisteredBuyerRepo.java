package com.eg.eclothing.repo;

import com.eg.eclothing.entity.UnregisteredBuyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnregisteredBuyerRepo extends JpaRepository<UnregisteredBuyer, Long> {
}
