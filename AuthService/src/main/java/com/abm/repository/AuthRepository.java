package com.abm.repository;

import com.abm.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth,Long> {


    /**
     * username password vt'da kayıtlı mı kontrolü yapar
     */

    Optional<Auth> findOptionalByUsernameIgnoreCaseAndPassword(String username, String password);


    boolean existsByUsernameIgnoreCase(String username);

    Optional<Auth> findOptionalByEmail(String email);

    Optional<Auth> findByEmailAndRepasswordcode(String email,String repasswordcode);
}

