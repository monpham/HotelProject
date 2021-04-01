package com.nhat.demo.service;

import com.nhat.demo.entity.Service;
import org.springframework.data.domain.Page;

public interface HotelSVServiceIF {

    Page<Service> getAllserviceByPage(int pageNumber);
}
