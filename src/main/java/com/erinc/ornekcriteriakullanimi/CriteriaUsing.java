package com.erinc.ornekcriteriakullanimi;

import com.erinc.repository.entity.Musteri;
import com.erinc.repository.entity.Urun;
import com.erinc.utility.HibernateUtility;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;

public class CriteriaUsing {
    private EntityManager entityManager;
    private CriteriaBuilder builder;


    public CriteriaUsing(){
        entityManager = HibernateUtility.getSessionFactory().createEntityManager();
        builder = entityManager.getCriteriaBuilder();
    }

    public List<Musteri> findAll(){
        // öncelikle kullanılacak sınıf criter e işlenir.
        CriteriaQuery<Musteri>criteria = builder.createQuery(Musteri.class);
        // hangi entity(tablo) varlığına select atılacak o belirlenir.
        Root<Musteri> root = criteria.from(Musteri.class);
        // select işlemi tanımlanır.
        criteria.select(root);
        List<Musteri> musteriList = entityManager.createQuery(criteria).getResultList();
        musteriList.forEach(x->{
            System.out.println(x.getId()+ " - "+x.getAd());
        });
        return musteriList;
    }

    public void selectOneColumn(){
        /**
         * select ad from tblmusteri where
         */
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<Musteri> root = criteria.from(Musteri.class);
        criteria.select(root.get("ad"));
        criteria.where(builder.equal(root.get("soyad"),"Kocaoglu"));
        List<String> adListesi = entityManager.createQuery(criteria).getResultList();
        adListesi.forEach(System.out::println);
    }

    public void selectManyColumn(){
        /**
         * Select id, ad, soyad from tblmusteri
         */
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Musteri> root = criteria.from(Musteri.class);

        Path<Long> idPath = root.get("id");
        Path<String> adPath = root.get("ad");
        Path<String> soyadPath = root.get("soyad");

        criteria.select(builder.array(idPath,adPath,soyadPath));

        List<Object[]> mlist = entityManager.createQuery(criteria).getResultList();
        mlist.forEach(x->{
            System.out.println("Data....: "+ x);
        });
    }

    public void usingTuple(){
        CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);
        Root<Musteri> root = criteria.from(Musteri.class);
        Path<Long> idPath = root.get("id");
        Path<String> adPath = root.get("ad");
        Path<String> soyadPath = root.get("soyad");
        criteria.multiselect(idPath,adPath,soyadPath);
        List<Tuple>tupleList = entityManager.createQuery(criteria).getResultList();
        tupleList.forEach(x->{
            System.out.println("id.....: " + x.get(idPath));
            System.out.println("ad.....: " + x.get(1));
            System.out.println("soyad.....: " + x.get(2));
        });

    }

    public List<Tuple> multipleRoot(){
        CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);

        Root<Musteri> musteriRoot = criteria.from(Musteri.class);
        Root<Urun> urunRoot = criteria.from(Urun.class);

        criteria.multiselect(musteriRoot,urunRoot);

        List<Tuple> tupleList = entityManager.createQuery(criteria).getResultList();

        return tupleList;
    }

    public void usingParameter(String musteriAdi){
        CriteriaQuery<Musteri> criteria = builder.createQuery(Musteri.class);
        Root<Musteri> root = criteria.from(Musteri.class);

        ParameterExpression<String> nickName = builder.parameter(String.class);
        criteria.where(builder.equal(root.get("ad"),nickName));

        TypedQuery<Musteri> query = entityManager.createQuery(criteria);
        query.setParameter(nickName,musteriAdi);
        List<Musteri> musteriList = query.getResultList();
        musteriList.forEach(x->{
            System.out.println(x.getId() + " -> "+ x.getAd() + " -> " + x.getSoyad());
        });

    }

    public void usingPredicate(){
        /**
         * select * from tblmusteri where ad like 'm%' and soyad is not null and id>2
         * olsun mesela...
         */
        CriteriaQuery<Musteri> criteria = builder.createQuery(Musteri.class);
        Root<Musteri> root = criteria.from(Musteri.class);

        Predicate predicateMusteri1 = builder.and(
                builder.like(root.get("ad"),"S%"),
                builder.isNotNull(root.get("soyad"))
        );
        Predicate predicateMusteri2 = builder.and(
                builder.greaterThan(root.get("id"),3)
        );

        criteria.where(builder.or(predicateMusteri1,predicateMusteri2));

        List<Musteri> musteriList = entityManager.createQuery(criteria).getResultList();
        musteriList.forEach(x->{
            System.out.println(x.getId()+ " -> "+ x.getAd());
        });



    }






}
