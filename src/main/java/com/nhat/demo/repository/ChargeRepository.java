package com.nhat.demo.repository;

import com.nhat.demo.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChargeRepository extends JpaRepository<Charge, Integer> {
    @Query(nativeQuery = true,value = " Select * from charge where booking_id = ?1")
    List<Charge> getListChargeByBookingId(int bookingId);

}
