package com.abm.service;

import com.abm.entity.Yetki;
import com.abm.repository.YetkiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YetkiService {
    private final YetkiRepository yetkiRepository;

    public Yetki save(Yetki yetki) {
        return yetkiRepository.save(yetki);
    }

    public List<Yetki> findAll() {
        return yetkiRepository.findAll();
    }

    public List<Yetki> findAllByAuthid(Long id) {
        return yetkiRepository.findAllByAuthid(id);
    }
}
