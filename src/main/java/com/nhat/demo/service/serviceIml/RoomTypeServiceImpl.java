package com.nhat.demo.service.serviceIml;

import com.nhat.demo.entity.RoomType;
import com.nhat.demo.repository.RoomTypeRepository;
import com.nhat.demo.service.RoomTypeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoomTypeServiceImpl implements RoomTypeServiceIF {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Override
    public List<RoomType> getAllRoomType() {
        return roomTypeRepository.findAll();
    }

    @Override
    public RoomType getRoomType(int roomTypeId) {
        return roomTypeRepository.findById(roomTypeId).orElse(null);
    }

    @Override
    public Page<RoomType> getAllRoomTypePage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,3);
        Page<RoomType> roomTypes = roomTypeRepository.findAll(pageable);
        return roomTypes;
    }

    @Override
    public List<RoomType> getSearchRoomType(String searchText) {
        List<RoomType> roomTypes = roomTypeRepository.getSearchRoomType(searchText);
        return  roomTypes;
    }
}
