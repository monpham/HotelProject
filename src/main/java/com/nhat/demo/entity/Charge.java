package com.nhat.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chargeId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime chargeDate;
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @Transient
    private double total;



    @PrePersist
    public void initChargeDate() {
        this.chargeDate = LocalDateTime.now();
    }


    @PostLoad
    public void postLoad() {
        this.total = quantity * service.getUnitPrice();

    }

//testing purpose
    @Override
    public String toString() {
        return "Charge{" +
                "chargeId=" + chargeId +
                ", chargeDate=" + chargeDate +
                ", quantity=" + quantity +
                ", booking=" + booking +
                ", service=" + service +
                '}';
    }

    public String convertTotalToString(Double total){
        String totalTypeString = Double.toString(total);
        return totalTypeString;
    }

    public String convertQuantityToString(int quantity){
        String quantityTypeString = Integer.toString(quantity);
        return quantityTypeString;
    }
}
