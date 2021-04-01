package com.nhat.demo.service;

import com.nhat.demo.entity.Room;
import com.nhat.demo.entity.RoomType;
import com.nhat.demo.model.BookingCart;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface RoomServiceIF {

    List<Room> getAvailableRoom(LocalDate checkInDate, LocalDate checkOutDate, int adults, int children);

    List<Room> getAvailableRoomByType(LocalDate checkInDate, LocalDate checkOutDate, int adults, int children, int roomTypeId);

    List<Integer> getDuplicateRoom(BookingCart bookingCart);

    Room getRoomById(int roomId);

    void removeRoom(int roomId);

    Page<Room> getAllRoom(int pageNumber);

    List<Room> getRoomSearch(String searchText);


}
