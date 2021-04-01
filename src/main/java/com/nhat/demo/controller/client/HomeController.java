package com.nhat.demo.controller.client;

import com.nhat.demo.entity.Room;
import com.nhat.demo.model.BookingCart;
import com.nhat.demo.service.RoomServiceIF;
import com.nhat.demo.service.RoomTypeServiceIF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private BookingCart bookingCart;
    @Autowired
    private RoomTypeServiceIF roomTypeService;
    @Autowired
    private RoomServiceIF roomService;

    @GetMapping(value = "/")
    public String toIndexpage(Model model) {
        model.addAttribute("roomTypes", roomTypeService.getAllRoomType());
        return "client/index";

    }

    @PostMapping("/search-available-room")
    public String toAvailableRoomPage(Model model,
                                      @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate checkInDate,
                                      @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate checkOutDate,
                                      @RequestParam int adults,
                                      @RequestParam int children) {
        // lưu checkinDate,checkoutDate vao bookingCart
        bookingCart.setCheckInDate(checkInDate);
        bookingCart.setCheckOutDate(checkOutDate);

        List<Room> availableRoom = roomService.getAvailableRoom(checkInDate, checkOutDate, adults, children);

        //can loai bo nhung room da co trong cart.
        if (!bookingCart.getBookingItems().isEmpty()) {
            List<Room> filtedARoomList = availableRoom.stream()
                    .filter(room -> !bookingCart.getBookingItems().containsKey(room.getRoomId()))
                    .collect(Collectors.toList());
            model.addAttribute("availableRooms", filtedARoomList);

        } else {
            model.addAttribute("availableRooms", availableRoom);
        }

        return "client/available-room";

    } //da in clude


    @PostMapping("/search-available-room-byType")
    public String toAvailableRoomPageByType(Model model,
                                            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate checkInDate,
                                            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate checkOutDate,
                                            @RequestParam int adults,
                                            @RequestParam int children,
                                            @RequestParam int roomTypeId) {

        log.info("#### {}",roomTypeId );


        // lưu checkinDate,checkoutDate vao bookingCart
        bookingCart.setCheckInDate(checkInDate);
        bookingCart.setCheckOutDate(checkOutDate);

        List<Room> availableRoom = roomService.getAvailableRoomByType(checkInDate, checkOutDate, adults, children, roomTypeId);

        //can loai bo nhung room da co trong cart (de tranh trung lap)
        if (!bookingCart.getBookingItems().isEmpty()) {
            List<Room> filtedARoomList = availableRoom.stream()
                    .filter(room -> !bookingCart.getBookingItems().containsKey(room.getRoomId()))
                    .collect(Collectors.toList());
            model.addAttribute("availableRooms", filtedARoomList);

        } else {
            model.addAttribute("availableRooms", availableRoom);
        }

        return "client/available-room";

    }

    @GetMapping("/room-detail")
    public String toRoomDetailPage(Model model,
                                   @RequestParam int roomId) {
        model.addAttribute("room", roomService.getRoomById(roomId));
        return "client/room-detail";
    }

    @GetMapping("/room-type-detail")
    public String toRoomTypeDetailPage(Model model,
                                   @RequestParam int roomTypeId) {
        model.addAttribute("roomType", roomTypeService.getRoomType(roomTypeId));
        return "client/room-type-detail";
    }


}
