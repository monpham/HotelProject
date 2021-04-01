package com.nhat.demo.repository;

import com.nhat.demo.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    @Query(nativeQuery = true, value = "select * from promotion where binary promotion_code = ?1")
    Promotion findByPromotionCode(String promotionCode);

}
