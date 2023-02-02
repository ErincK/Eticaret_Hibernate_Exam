package com.erinc.ornekcriteriakullanimi;

import com.erinc.repository.entity.Musteri;
import com.erinc.repository.entity.Urun;
import com.erinc.utility.HibernateUtility;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.mapping.Map;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public void groupBy(){
        CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);
        Root<Musteri> root = criteria.from(Musteri.class);

        criteria.groupBy(root.get("ad"));
        criteria.multiselect(root.get("ad"),builder.count(root));

        List<Tuple> list = entityManager.createQuery(criteria).getResultList();

        list.forEach(x->{
            System.out.println("ad....: "+ x.get(0) + " -> "+ x.get(1));
        });
    }

    public void findAllNativeQuery(){
        /**
         * Buraya kadar;
         * Java Persistence API üzerinden hibernate ile SQL sorgularını hazırlayıp çalıştırdık.
         * Tüm ORM araçlarında kullanılan yapıların yetersiz kalabileceği durumlar olabilir yada
         * yazılan kodlar karmaşıklaşarak odaktan uzaklaşmaya neden olabilir.
         * Bütün bu sebeplerden dolayı, belli sorgularda ham SQL komutları çalıştırmak isteyebiliriz.
         *
         *
         */
        List<Object[]> mlist = entityManager.createNativeQuery("select id,musteriad,soyad,email,cinsiyet from tblmusteri").getResultList();
        mlist.forEach(x->{
            System.out.println(Arrays.asList(x).get(0) + " - " +Arrays.asList(x).get(1));
        });




    }

    public void namedQueryFindAll(){
        TypedQuery<Musteri> result = entityManager.createNamedQuery("Musteri.findAll", Musteri.class);
        List<Musteri> mlist = result.getResultList();
        mlist.forEach(x->{
            System.out.println(x.getId()+" "+x.getAd());
        });
    }

    /**
     * Musteri adına göre arama yapan method'dur.
     * musteri adını verirken, filtrelemek için % karakteri kullanılır.
     */
    public void namedQueryFindByAd(String ad){
        TypedQuery<Musteri> typedQuery = entityManager.createNamedQuery("Musteri.findByAd", Musteri.class);
        typedQuery.setParameter("benbirmusteriadiistiyorum",ad);
        List<Musteri> musteriList = typedQuery.getResultList();
        musteriList.forEach(x->{
            System.out.println(x.getId()+" -> "+ x.getAd() + " "+ x.getSoyad());
        });
    }

    public void namedQueryFindById(Long id){
        TypedQuery<Musteri>typedQuery =
                entityManager.createNamedQuery("Musteri.findById", Musteri.class);
        typedQuery.setParameter("musteriid", id);
        Optional<Musteri> result;
        try{
            Musteri musteri = typedQuery.getSingleResult();
            result = Optional.of(musteri);
        }catch(Exception e){
            System.out.println("Hata vaaaar..... : " + e.toString());
            result = Optional.empty();
        }
        if (result.isPresent())
        System.out.println(result.get().getId()+" --> "+ result.get().getAd()+" - "+result.get().getSoyad());
    }

    public void namedQueryGetCount(){
        TypedQuery<Long> typedQuery = entityManager.createNamedQuery("Musteri.getCount", Long.class);
        Long count = typedQuery.getSingleResult();
        System.out.println("Musteri sayısı....: "+ count);
    }

    public void typedQuerySetProperties(int page, int count){
        /**
         * Pagination -> sayfalama
         */
        TypedQuery<Musteri> typedQuery = entityManager.createNamedQuery("Musteri.findAll",Musteri.class);
        typedQuery.setMaxResults(count);
        typedQuery.setFirstResult(page*count);
        List<Musteri> musteriList = typedQuery.getResultList();
        musteriList.forEach(x->{
            System.out.println(x.getId()+" "+x.getAd());
        });
    }







}
