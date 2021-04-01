package com.nhat.demo.service;

import com.nhat.demo.entity.RoomType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomTypeServiceIF {

    List<RoomType> getAllRoomType();

    RoomType getRoomType(int roomTypeId);

    List<RoomType> getSearchRoomType(String searchText);

    Page<RoomType> getAllRoomTypePage(int pageNumber);



}
