package com.erinc;

import com.erinc.ornekcriteriakullanimi.CriteriaUsing;
import com.erinc.repository.MusteriRepository;
import com.erinc.repository.entity.ECinsiyet;
import com.erinc.repository.entity.Musteri;
import com.erinc.repository.entity.Urun;
import com.erinc.utility.HibernateUtility;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EticaretApplication {
    public static void main(String[] args) {
    //save
    //list();
        CriteriaUsing cr = new CriteriaUsing();
        //cr.findAll();
        //cr.selectOneColumn();
        //cr.selectManyColumn();
        //cr.usingTuple();
        //saveMusteri();
        //cr.multipleRoot().forEach(x->{
        //    System.out.println(((Musteri)x.get(0)).toString());
        //    System.out.println(((Urun)x.get(1)).toString());
        //    System.out.println("****************************************");
        //});
        //cr.usingParameter("Erinc");
        //cr.usingPredicate();
        //cr.groupBy();
        //cr.findAllNativeQuery();
        //cr.namedQueryFindAll();
        //cr.namedQueryFindByAd("%h%");
        //cr.namedQueryFindById(3l);
        //cr.namedQueryFindById(100l);
        //cr.namedQueryGetCount();
        //cr.typedQuerySetProperties(1,3);
        Musteri musteri = Musteri.builder()
                .ad("Kahtali")
                .soyad("Mici")
                .adres("Holland")
                .cinsiyet(ECinsiyet.ERKEK)
                .build();
        MusteriRepository musteriRepository = new MusteriRepository();
        musteriRepository.save(musteri);
        musteriRepository.findAllByColumnNameAndValue("ad","Kahtali");


    }

    private static void criteriaList(){
        EntityManager entityManager = HibernateUtility.getSessionFactory().createEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        //Session ss = HibernateUtility.getSessionFactory().openSession();
        /**
         * Tüm Datayı Çekmek
         * criteriaQuery aslında bizim, Selecr * from tblmusteri sorgusunu ve bu sorgu neticesinde
         * dönecek olanların neler olduğunu çözebilmesi için hazırlanıyor.
         * select id,ad,soyad,cinsiyet,....  from tblmusteri
         */
        CriteriaQuery<Musteri> criteria = builder.createQuery(Musteri.class);
        Root<Musteri> root = criteria.from(Musteri.class);
        criteria.select(root);
        List<Musteri>listem = entityManager.createQuery(criteria).getResultList();
        listem.forEach(x->{
            System.out.println("ad.......: " + x.getAd());
        });
    }

    private static void list(){
        Session session = HibernateUtility.getSessionFactory().openSession();
        Criteria cr = session.createCriteria(Musteri.class);
        List<Musteri>musteriList = cr.list();
        session.close();
        musteriList.forEach(x->{
            System.out.println("Musteri Adi Soyadi...:" + x.getAd() +" "+ x.getSoyad());
        });
    }

    private static void saveMusteri(){
        Session session = HibernateUtility.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        List<String > telList= Arrays.asList("0555 555 33 45", "0999 555 43 65");
        Musteri musteri = Musteri.builder()
                .ad("Eva")
                .adres("Eindhoven")
                .soyad("Kocaoglu")
                .cinsiyet(ECinsiyet.ERKEK)
                .telefonlistesi(telList)
                .build();
        session.save(musteri);
        transaction.commit();
        session.close();
    }

    private static void saveUrun(){
        Session session = HibernateUtility.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Urun urun = Urun.builder()
                .kdv(18)
                .model("PSV")
                .marka("Nederland")
                .fiyat(120d)
                .ad("Kakao")
                .build();
        session.save(urun);
        transaction.commit();
        session.close();
    }


}