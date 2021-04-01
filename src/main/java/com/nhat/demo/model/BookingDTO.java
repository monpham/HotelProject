package com.nhat.demo.model;

import com.nhat.demo.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookingDTO {
    // danh cho person
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phone;
    private String email;
    private String indentifyNo;
    //danh cho creditcart

    private String ownerName;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;


    @Override
    public String toString() {
        return "BookingDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", indentifyNo='" + indentifyNo + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", expiryMonth='" + expiryMonth + '\'' +
                ", expiryYear='" + expiryYear + '\'' +
                '}';
    }
}
