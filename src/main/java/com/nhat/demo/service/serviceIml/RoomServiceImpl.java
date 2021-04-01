package com.nhat.demo.service.serviceIml;

import com.nhat.demo.entity.Room;
import com.nhat.demo.model.BookingCart;
import com.nhat.demo.repository.RoomRepository;
import com.nhat.demo.service.RoomServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomServiceIF {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> getAvailableRoom(LocalDate checkInDate, LocalDate checkOutDate, int adults, int children) {
        return roomRepository.findAvailableRoom(checkInDate, checkOutDate, adults, children);
    }

    @Override
    public List<Room> getAvailableRoomByType(LocalDate checkInDate, LocalDate checkOutDate, int adults, int children, int roomTypeId) {
        return roomRepository.findAvailableRoomByType(checkInDate, checkOutDate, adults, children, roomTypeId);
    }

    @Override
    public List<Integer> getDuplicateRoom(BookingCart bookingCart) {
        List<Integer> RoomNumber = bookingCart.getBookingItems()
                .values()
                .stream()
                .mapToInt(item -> item.getRoom().getRoomNumber()).boxed().collect(Collectors.toList());

        //danh sach roomNumber đã được dặt trong khoảng thời gian đó (ko thể đặt lại)
        List<Integer> bookedRoomNumber = roomRepository.findBookedRoom(bookingCart.getCheckInDate(), bookingCart.getCheckOutDate());

        return  RoomNumber.stream().filter(integer -> bookedRoomNumber.contains(integer)).collect(Collectors.toList());


    }

    @Override
    public Room getRoomById(int id) {
        return roomRepository.findById(id).orElse(null);
    }


    @Override
    public void removeRoom(int roomId) {
        roomRepository.deleteById(roomId);
    }

    @Override
    public Page<Room> getAllRoom(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,7);
        Page<Room> rooms = roomRepository.findAll(pageable);
        return rooms;
    }

    @Override
    public List<Room> getRoomSearch(String searchText) {
        List<Room> rooms = roomRepository.getSearchRoom(searchText);
        return rooms;
    }

}
