package com.nhat.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;
    private String bookingCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bookingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = true, unique = false)
    private Promotion promotion;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingDetail> bookingDetails = new ArrayList<>();

    @OneToMany(mappedBy = "booking")
    private List<Charge> charges = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "invoice_id", nullable = true, unique = true)
    private Invoice invoice;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_person_id")
    private BookingPerson bookingPerson;

    @OneToMany(mappedBy = "booking")
    private List<Transaction> transactions = new ArrayList<>();

    @Transient
    private double totalCharge;


    @Transient
    private double sumOfSubTotal;

    @Transient
    private double total;

    @PrePersist
    public void initbookingDate() {
        this.bookingDate = LocalDateTime.now();
    }

    @PostLoad
    public void postLoad() {
        this.sumOfSubTotal = bookingDetails.stream().mapToDouble(BookingDetail::getSubTotal).sum();
        if (promotion != null) {
            this.total = sumOfSubTotal - promotion.getPromotionValue();
        } else {
            this.total = sumOfSubTotal;
        }

        // tu dong tinh tong tien dich vu da su dung cho toan bo booking
        this.totalCharge = charges.stream().mapToDouble(Charge::getTotal).sum();
    }

    public String getLocalDateTimeFormatter(LocalDateTime time){
        String formatLocalDateTime = time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"));
        return formatLocalDateTime;
    }

    public String getLocalDateFormatter(LocalDate time){
        String formatLocalDate = time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return formatLocalDate;
    }

//    public String convertTotalChargeToString (double totalCharge){
//        String totalChargeTypeString = Double.toString(totalCharge));
//        return totalChargeTypeString;
//    }

    @Override
    public String toString() {
        return
                "Ma Booking : " + bookingId + "\n" +
                "Ten nguoi su dung :" + bookingPerson.getLastName() + " " + bookingPerson.getFirstName() + "\n" +
                "So dien thoai : " + bookingPerson.getPhone() + "\n" +
                "Tong tien cho cac dich vu ben duoi: " + totalCharge + " D";
    }

}
