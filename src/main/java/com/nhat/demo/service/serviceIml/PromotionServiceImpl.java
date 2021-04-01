package com.nhat.demo.service.serviceIml;

import com.nhat.demo.entity.Promotion;
import com.nhat.demo.repository.PromotionRepository;
import com.nhat.demo.service.PromotionServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PromotionServiceImpl implements PromotionServiceIF {
    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public Promotion getPromotionById(String promotionCode) {
        return promotionRepository.findByPromotionCode(promotionCode);
    }

    @Override
    public boolean checkStatusPromotion(String promotionCode) {
        Promotion promotion = promotionRepository.findByPromotionCode(promotionCode);
        if (promotion == null) {
            return false;
        }
        LocalDate startDate = promotion.getStartDate();
        LocalDate endDate = promotion.getEndDate();
        LocalDate now = LocalDate.now();
        return now.isAfter(startDate.minusDays(1))
                &&
                now.isBefore(endDate.plusDays(1));


    }
}
