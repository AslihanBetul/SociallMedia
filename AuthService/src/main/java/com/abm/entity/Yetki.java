package com.abm.entity;

import com.abm.entity.enums.EYetki;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.lang.annotation.Documented;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tblyetki")
public class Yetki {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    EYetki yetki;
    Long authid;

}
