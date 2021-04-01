package com.nhat.demo.model;

import com.nhat.demo.entity.Promotion;
import com.nhat.demo.service.RoomServiceIF;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)

public class BookingCart {
    @Autowired
    private RoomServiceIF roomService;


    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Map<Integer, BookingItem> bookingItems = new HashMap<>();
    private Promotion promotion = new Promotion();


    public double calculateTotal() {
        return bookingItems
                .values()
                .stream().
                        mapToDouble(item -> item.calculateSubTotal(checkInDate, checkOutDate))
                .sum() - this.promotion.getPromotionValue();
    }


    //khi nguoi dung an "add to cart"
    public void addItem(BookingItem bookingItem) {
        this.bookingItems.put(bookingItem.getRoom().getRoomId(), bookingItem);
    }

    //khi nguoi dung an "xoa"
    public void removeItem(int roomId) {
        this.bookingItems.remove(roomId);
    }

    //xoa toan to item
    public void removeALlItem() {
        this.bookingItems.clear();
    }


}
