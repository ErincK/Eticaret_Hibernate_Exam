package com.erinc.repository;

import com.erinc.repository.entity.Urun;
import com.erinc.utility.MyFactoryRepository;

public class UrunRepository extends MyFactoryRepository<Urun, Long> {

    public UrunRepository() {
        super(new Urun());
    }
}
