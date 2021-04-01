package com.nhat.demo.service;

import com.nhat.demo.entity.Promotion;

public interface PromotionServiceIF {


        Promotion getPromotionById(String promotionCode);

        boolean checkStatusPromotion(String promotionCode);



}
