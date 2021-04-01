package com.nhat.demo.repository;

import com.nhat.demo.entity.RoomTypeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface RoomtypeImageRepository  extends JpaRepository<RoomTypeImage, Integer> {
    @Modifying
    @Transactional
    @Query(value = "delete from room_type_image r where room_type_id = ?1 ;",nativeQuery = true)
    void deleteRoomTypeImageFromRoomTypeId(int id);
}
