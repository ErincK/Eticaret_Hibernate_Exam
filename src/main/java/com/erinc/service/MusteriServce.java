package com.erinc.service;

import com.erinc.repository.MusteriRepository;
import com.erinc.repository.entity.Musteri;
import com.erinc.utility.IService;
import com.erinc.utility.MyFactoryService;

import java.util.List;
import java.util.Optional;

public class MusteriServce extends MyFactoryService<MusteriRepository, Musteri, Long> {


    public MusteriServce() {
        super(new MusteriRepository());
    }
}
