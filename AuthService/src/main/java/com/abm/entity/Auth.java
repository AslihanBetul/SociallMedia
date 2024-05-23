package com.abm.entity;

import com.abm.enums.AuthRole;
import com.abm.enums.AuthStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;


import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
@Data
@Entity
@Table(name = "tbl_auth")

public class Auth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    @Length(min = 3,max = 30)
    String username;
    @Length(min = 8)
    String password;
    String repasswordcode;
    @Email
    String email;
    @Builder.Default
    String activationcode= UUID.randomUUID().toString();
    @Builder.Default
    @Enumerated(EnumType.STRING)
    AuthStatus status=AuthStatus.PENDING;

    AuthRole role;
}
