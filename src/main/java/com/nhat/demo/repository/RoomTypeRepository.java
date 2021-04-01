package com.nhat.demo.repository;

import com.nhat.demo.entity.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    @Query(nativeQuery = true,
            value = "select * from room_type where room_type_id >= ?1")
    List<RoomType> findAllRoomType(int id);

    RoomType findByTypeName(String typeName);

    Page<RoomType> findAll(Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM room_type r where\n" +
                    "r.type_name like %?1% or r.unit_price like %?1% or r.adult_capacity like %?1% or r.children_capacity like %?1% ;")
    List<RoomType> getSearchRoomType(String searchText);
}
