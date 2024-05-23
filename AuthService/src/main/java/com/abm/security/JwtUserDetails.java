package com.abm.security;

import com.abm.entity.Auth;

import com.abm.service.AuthService;

import com.abm.service.YetkiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {
    private final AuthService authService;
    private final YetkiService yetkiService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }




    public UserDetails loadUserByAuthid(Long authid) {
        Auth auth = authService.findById(authid);

        List<GrantedAuthority>authorities=new ArrayList<>();

        yetkiService.findAllByAuthid(auth.getId()).forEach(yetki->{
            authorities.add(new SimpleGrantedAuthority(yetki.getYetki().name()));
        });


        return  User.builder()
                .username(auth.getUsername())
                .password("")
                .authorities(authorities)
                .build();

    }
}
