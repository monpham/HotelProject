package com.nhat.demo.service;

import com.nhat.demo.entity.Booking;

import java.util.List;

public interface BookingServiceIF {
    List<Booking> getAllBooking();

    Booking getBookingById(int bookingId);

    Booking getBookingByBookingCode(String bookingCode);


    void saveBooking(Booking booking);

    String createBookingCode();

    void removeBookingByPromotionCode(String bookingCode);

    List<Booking> getCurrentStayBooking();


}
