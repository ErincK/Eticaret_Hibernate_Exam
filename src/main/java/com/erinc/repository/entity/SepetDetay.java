package com.erinc.repository.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tblsepetdetay")
public class SepetDetay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long sepetid;
    Long urunid;
    Integer adet;
    Double toplamfiyat;
    Integer kdv;
    Double kdvtutari;
    @Embedded
    BaseEntity baseEntity;
}
