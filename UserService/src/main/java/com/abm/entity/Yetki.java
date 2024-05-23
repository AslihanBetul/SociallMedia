package com.abm.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
