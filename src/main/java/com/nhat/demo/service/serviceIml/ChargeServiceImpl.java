package com.nhat.demo.service.serviceIml;

import com.nhat.demo.entity.Charge;
import com.nhat.demo.repository.ChargeRepository;
import com.nhat.demo.service.ChargeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChargeServiceImpl implements ChargeServiceIF {

    @Autowired
    private ChargeRepository chargeRepository;
    @Override
    public void addCharge(Charge charge) {
        chargeRepository.save(charge);

    }
}
