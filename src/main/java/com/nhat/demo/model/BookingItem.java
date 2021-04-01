package com.nhat.demo.model;

import com.nhat.demo.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Setter
@Getter
@NoArgsConstructor

public class BookingItem {
    private Room room;
    private int adults;
    private int children;

    public BookingItem(Room room, int children, int adults) {
        this.room = room;
        this.adults = adults;
        this.children = children;
    }


    public double calculateSubTotal(LocalDate checkIndate, LocalDate checkOutDate) {

        return   countNight(checkIndate, checkOutDate) *     this.room.getRoomType().getUnitPrice();


    }

    public int countNight(LocalDate checkIndate, LocalDate checkOutdate) {
        return (int) ChronoUnit.DAYS.between(checkIndate, checkOutdate);

    }
}
