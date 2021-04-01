package com.nhat.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.temporal.ChronoUnit;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingDetailId;
    private int adults;
    private int chidren;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", unique = false, nullable = false)
    private Booking booking;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", unique = false, nullable = false)
    private Room room;

    @Transient
    private int night;

    @Transient
    private double subTotal;

    @PostLoad
    public void postLoad() {
        this.night = (int) ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        this.subTotal = night * room.getRoomType().getUnitPrice();
    }




}
