package com.nhat.demo.controller.client;

import com.nhat.demo.entity.Promotion;
import com.nhat.demo.entity.Room;
import com.nhat.demo.model.BookingCart;
import com.nhat.demo.model.BookingItem;
import com.nhat.demo.service.PromotionServiceIF;
import com.nhat.demo.service.RoomServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class BookingCartController {
    @Autowired
    private RoomServiceIF roomService;
    @Autowired
    private BookingCart bookingCart;
    @Autowired
    private PromotionServiceIF promotionService;


    //khi nguoi dung an "add to cart" tai trang room-booking
    @PostMapping("/addRoom")
    public String addToCard(@RequestParam int roomId,
                            @RequestParam int adults,
                            @RequestParam int children
    ) {
        //lay room tu duoi database len
        Room room = roomService.getRoomById(roomId);

        // tao mot bookingItem
        BookingItem bookingItem = new BookingItem(room, children, adults);

        //add vao cart
        bookingCart.addItem(bookingItem);
        return "redirect:/cart/showCart";

    }

    @GetMapping("/removeRoom")
    public String removeRoom(@RequestParam int roomId) {
        bookingCart.removeItem(roomId);
        if (bookingCart.getBookingItems().size() == 0) {
            bookingCart.setPromotion(new Promotion());
        }
        return "redirect:/cart/showCart";
    }

    @PostMapping("/checkPromotion")
    public String checkPromotion(@RequestParam String promotionCode, Model model) {
        //kiem tra xem co duoi dababase khong
        boolean isVaid = promotionService.checkStatusPromotion(promotionCode);
        if (isVaid) {
            //ton tai ma giam gia do
            // dua promotion vao cart
            Promotion promotion = promotionService.getPromotionById(promotionCode);
            bookingCart.setPromotion(promotion);

        } else {
            model.addAttribute("message", "mã giảm giá không tồn tại hoặc quá hạn sử dụng");
        }


        return "/client/booking-information";
    }

    @GetMapping("/showCart")
    public String showCartPage() {

        return "/client/booking-information";
    }
}
