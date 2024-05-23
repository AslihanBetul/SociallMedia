package com.abm.repository;

import com.abm.entity.Yetki;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YetkiRepository extends JpaRepository<Yetki,Long> {
    List<Yetki> findAllByAuthid(Long id);
}
