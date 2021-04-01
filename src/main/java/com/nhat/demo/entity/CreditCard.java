package com.nhat.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cardId;
    private String ownerName;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private double balance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return ownerName.equals(that.ownerName) &&
                cardNumber.equals(that.cardNumber) &&
                expiryMonth.equals(that.expiryMonth) &&
                expiryYear.equals(that.expiryYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerName, cardNumber, expiryMonth, expiryYear);
    }
}
