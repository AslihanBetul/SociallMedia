package com.abm.security;

import com.abm.entity.UserProfile;
import com.abm.service.UserProfileService;
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
    private final UserProfileService userProfileService;
    private final YetkiService yetkiService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }




    public UserDetails loadUserByAuthid(Long authid) {
        UserProfile userProfile = userProfileService.findByAuthId(authid.toString());

        List<GrantedAuthority>authorities=new ArrayList<>();

        yetkiService.findAllByAuthid(Long.parseLong(userProfile.getId())).forEach(yetki->{
            authorities.add(new SimpleGrantedAuthority(yetki.getYetki().name()));
        });


        return  User.builder()
                .username(userProfile.getUsername())
                .password("")
                .authorities(authorities)
                .build();

    }
}
