package com.nhat.demo.service.serviceIml;

import com.nhat.demo.entity.Service;
import com.nhat.demo.repository.ServiceRepository;
import com.nhat.demo.service.HotelSVServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@org.springframework.stereotype.Service
public class HotelSVServiceImpl implements HotelSVServiceIF {
    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public Page<Service> getAllserviceByPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        Page<Service> services = serviceRepository.findAll(pageable);
        return services;
    }
}
