package com.nhat.demo.service;

import com.nhat.demo.entity.CreditCard;

public interface CreditCardServiceIF {
    String HOTELCARD = "387430430249384";

    String ValidateCart(CreditCard creditCard);

    void tranferMoney(String fromCard, String toCard, double amount);



}
