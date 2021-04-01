package com.nhat.demo.repository;

import com.nhat.demo.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    CreditCard findByCardNumber(String cartNumber);
}
