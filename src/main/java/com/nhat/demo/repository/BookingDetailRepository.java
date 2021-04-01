package com.nhat.demo.repository;

import com.nhat.demo.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {

@Query(nativeQuery = true, value = "select * from booking_detail where room_id = ?1")
        List<BookingDetail> getListBookingDetailByRoomId(int roomid);


}
