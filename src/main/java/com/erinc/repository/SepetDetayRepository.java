package com.erinc.repository;

import com.erinc.repository.entity.SepetDetay;
import com.erinc.utility.MyFactoryRepository;

public class SepetDetayRepository extends MyFactoryRepository<SepetDetay, Long> {
    public SepetDetayRepository() {
        super(new SepetDetay());
    }
}
