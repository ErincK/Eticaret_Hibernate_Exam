package com.erinc.repository.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Builder // Builder, bir sınıftan nesne üretmek için özel oluşturulmuş bir method.
@Data // Data, get, set methodlarını tanımlar
@NoArgsConstructor // Parametresiz constructor tanımlar
@AllArgsConstructor // 1...n kadar olan tüm parametreli constructorları tanımlar
@ToString // sınıf
public class Iletisim {
    String telefon;
    String email;
    String web;
    String instagram;

}
