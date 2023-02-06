package com.erinc.repository;

import com.erinc.repository.entity.Sepet;
import com.erinc.utility.MyFactoryRepository;

public class SepetRepository extends MyFactoryRepository<Sepet, Long> {
    public SepetRepository() {
        super(new Sepet());
    }
}
