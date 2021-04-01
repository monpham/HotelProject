package com.nhat.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class BookingPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingPersonId;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phone;
    private String email;
    private String indentifyNo;
    @OneToOne(mappedBy = "bookingPerson", fetch =FetchType.LAZY )
    private Booking booking;

    private String CardNumber;

}
