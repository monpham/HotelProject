package com.nhat.demo.controller.client;


import com.nhat.demo.entity.*;
import com.nhat.demo.model.BookingCart;
import com.nhat.demo.model.BookingDTO;
import com.nhat.demo.model.BookingItem;
import com.nhat.demo.service.BookingServiceIF;
import com.nhat.demo.service.CreditCardServiceIF;
import com.nhat.demo.service.RoomServiceIF;
import com.nhat.demo.service.RoomTypeServiceIF;
import com.nhat.demo.service.serviceIml.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class BookingController {
    @Autowired
    private CreditCardServiceIF creditCardService;
    @Autowired
    private BookingServiceIF bookingService;
    @Autowired
    private BookingCart bookingCart;

    @Autowired
    private EmailService emailService;
    @Autowired
    private RoomServiceIF roomService;

    @Autowired
    private RoomTypeServiceIF roomTypeService;





    // xac nhan lai thong tin adults, children khi dat mot phong
    @GetMapping("/room-booking")
    public String toRoomBookingPages(@RequestParam int roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        int childrenCapacity = room.getRoomType().getChildrenCapacity();
        int adultCapacity = room.getRoomType().getAdultCapacity();
        model.addAttribute("childrenCapacity", childrenCapacity);
        model.addAttribute("adultCapacity", adultCapacity);
        model.addAttribute("room", room);


        return "client/room-booking";
    }

    @GetMapping("/book-by-type")
    public String toBookByTypePage(@RequestParam int roomTypeId, Model model) {
    // hien thi thong tin co ban cua loai phong
        model.addAttribute("roomType", roomTypeService.getRoomType(roomTypeId));
        return "client/book-by-type";
    }


    @GetMapping("/booking-done/{bookingCode}")
    public String toBookingDonePage(@PathVariable String bookingCode, Model model) {
        Booking booking = bookingService.getBookingByBookingCode(bookingCode);
        model.addAttribute("booking", booking);
        return "client/booking-done";
    }


    @GetMapping("/booking-info")
    public String toBokingInfo(@RequestParam String bookingCode, Model model) {
        Booking booking = bookingService.getBookingByBookingCode(bookingCode);
        //phan nay dung de tao mot bien lam can cu de cho phep huy phong hay khong
        LocalDate now = LocalDate.now();
        LocalDate checkInDate = booking.getCheckInDate();
        model.addAttribute("isBeforeCheckInDate", now.isBefore(checkInDate));
        model.addAttribute("booking", booking);
        return "client/booking-done-info";
    }

    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/cancel-booking")
    public String toCancelBooking(@RequestParam String bookingCode) {

        // hoan lai 80% cho khach hang
        Booking booking = bookingService.getBookingByBookingCode(bookingCode);
        String cardNumber = booking.getBookingPerson().getCardNumber();
        double amount = booking.getTotal() * 0.8;

        creditCardService.tranferMoney(CreditCardServiceIF.HOTELCARD, cardNumber, amount);
        //xoa bo booking
        bookingService.removeBookingByPromotionCode(bookingCode);
        return "client/cancel-booking";
    }


    // API kiem tra ma dong phong co ton tai hay khong
    @PostMapping("/lookup")
    @ResponseBody
    public String tocheckExistBookingCode(@RequestParam String bookingCode) {
        Booking booking = bookingService.getBookingByBookingCode(bookingCode);
        if (booking == null) {
            return "not found";
        }
        return "found";
    }


    @GetMapping("/lookup")
    public String toLookUpForm() {
        return "client/lookup";
    }

    // khi ấn thanh toán
    @PostMapping("/booking-process")
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    public Object bookingProcess(BookingDTO bookingDTO) throws MessagingException {

        //tao CreditCard tu BookingDTO
        CreditCard creditCard = new CreditCard();
        creditCard.setOwnerName(bookingDTO.getOwnerName());
        creditCard.setCardNumber(bookingDTO.getCardNumber());
        creditCard.setExpiryMonth(bookingDTO.getExpiryMonth());
        creditCard.setExpiryYear(bookingDTO.getExpiryYear());

        // kiem tra tinh trang cua CreditCart
        String result = creditCardService.ValidateCart(creditCard);
        if (result.equals("not match") || result.equals("not enough")) {
            return result;
        }

        // khi the hop le thi tien hanh kiem tra phong trong mot lan nua
        List<Integer> duplicateRoom = roomService.getDuplicateRoom(bookingCart);
        if ( !duplicateRoom.isEmpty()) {
            return duplicateRoom;

        }


            // chuyen khoan (tru tien khach hang ==> tang tien cua chu khach san

        creditCardService.tranferMoney(creditCard.getCardNumber(), CreditCardServiceIF.HOTELCARD, bookingCart.calculateTotal());

        //ta mot booking rooom, tao n boookingdetail, tao 1 person

        // tao mot random String
        String bookingCode = bookingService.createBookingCode();

        //tao mot booking object
        Booking booking = new Booking();
        booking.setBookingCode(bookingCode);
        // KHÔNG LƯU TRỮ BOOKING PRICE
        booking.setCheckInDate(bookingCart.getCheckInDate());
        booking.setCheckOutDate(bookingCart.getCheckOutDate());
        // set  bang promotion nếu có promotion
        if (bookingCart.getPromotion().getPromotionId() != 0) {
            booking.setPromotion(bookingCart.getPromotion());
        }


        // tao mot bookingperson object
        BookingPerson bookingPerson = new BookingPerson();
        bookingPerson.setFirstName(bookingDTO.getFirstName());
        bookingPerson.setLastName(bookingDTO.getLastName());
        bookingPerson.setGender(bookingDTO.getGender());
        bookingPerson.setEmail(bookingDTO.getEmail());
        bookingPerson.setPhone(bookingDTO.getPhone());
        bookingPerson.setIndentifyNo(bookingDTO.getIndentifyNo());
        bookingPerson.setCardNumber(bookingDTO.getCardNumber());

        //setbookingperson cho booking
        booking.setBookingPerson(bookingPerson);
        //set booking cho bookingperson
        bookingPerson.setBooking(booking);


        //convert BookingItem sang bookingDetail save vao database
        Map<Integer, BookingItem> bookingItems = bookingCart.getBookingItems();
        for (BookingItem item : bookingItems.values()) {
            BookingDetail bookingDetail = new BookingDetail();
            bookingDetail.setAdults(item.getAdults());
            bookingDetail.setChidren(item.getChildren());
            // set booking cho cac bookingDetail
            bookingDetail.setBooking(booking);
            //set room cho cac bookDetail
            bookingDetail.setRoom(item.getRoom());
            //set bookingdetail cho cac booking
            booking.getBookingDetails().add(bookingDetail);
        }

        //save booking thi se tu dong luu bookingperson va bookingDetail
        //vi da dung cascade = all
        bookingService.saveBooking(booking);


        // gửi email

        emailService.sendMail(bookingPerson, bookingCode);

        // xoa tat ca moi thu trong gio hang
        bookingCart.setPromotion(new Promotion());
        bookingCart.setCheckOutDate(null);
        bookingCart.setCheckOutDate(null);
        bookingCart.setBookingItems(new HashMap<>());


        return bookingCode;

    }

}

