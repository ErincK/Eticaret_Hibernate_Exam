package com.erinc.repository.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tblmusteri", schema = "public")
public class Musteri {
    /**
     * Tablolarda ID için benzersiz olan oluşturma yöntemleri
     * 1- Otomatik SQ ile oluşturma; (IDENTITY, TABLO, SEQUENCE, AUTO)
     * 2- Elle SQ oluşturarak atama,
     * 3- UUID oluşturarak elle atama
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @SequenceGenerator(
            name="sq_ozel_musteri_id",sequenceName = "sq_ozel_musteri_id",
            allocationSize = 5, initialValue = 1000
    )
    @GeneratedValue(
            generator = "sq_ozel_musteri_id")
    Long musteri_id;
    @GenericGenerator(name = "name_uuid",strategy = "uuid2")
    @GeneratedValue(generator = "name_uuid")
    UUID uuid;
    /**
     * Tablolarda alanların özelleştirilmesi;
     */
    @Column(
            name = "musteriad",
            length = 100,
            nullable = false, //Bu alan boş geçilemez demektir.
            unique = false, // eğer true olur ise, bu alana aynı ad ile kayıt olamaz.
            insertable = true, //bunu false yaparsanız ekleme yapamazsınız.
            updatable = false  // update edilebilirliği false hale getirirseniz bu alan güncellenemez.
    )
    String ad;
    @Column(length = 100)
    String soyad;
    @Lob
    String adres;
    /**
     * TemporalType.DATE -> tarih belirtirken
     * TemporalType.TIME -> zamanı,saati belirtirken
     * TemporalType.TIMESTAMP -> Zaman damgası için (System.currentMillis())
     */
    @Temporal(TemporalType.DATE)
    Date dogumtarihi;
    /**
     * DİKKAT!! üzerine özellikle belirtmedikçe tüm tanımlar HIBERNATE tarafından
     * tabloda kolon olarak oluşturulur.
     * Eğer bir alanı kolon olarak oluşturmak istemiyorsanız bunu, @Transient ekleyerek yaparız.
     */

    @Transient
    String adsoyad;
    /**
     * Tablolar içinde eğer liste şeklinde bilgi saklamak istiyor isek, bunu belirtmeliyiz.
     * Hibernate liste şeklinde tutulacak alanlar için bağlam yapmak zorunda kalır.
     * Bunu belirtmek için @ElementCollection kullanmalısınız.
     */
    @ElementCollection
    List<String> telefonlistesi ;
    /**
     * Başka bir sınıfı bir varlık içinde kullanmak için
     * @Embedded kullanırız.
     */
    @Embedded
    Iletisim iletisim;
    @Embedded
    BaseEntity baseEntity;

    /**
     * Enum class'larınn hibernate tarafından işlenme şeklini
     * @Enumerated ile belirtiriz.
     * EnumType.STRING -> Enum bilgisi DB de String olarak tutulur.
     * EnumType.ORDINAL -> Enum bilgisi DB'de Sayısal tutulur.
     */
    @Enumerated(EnumType.STRING)
    ECinsiyet cinsiyet;

}
