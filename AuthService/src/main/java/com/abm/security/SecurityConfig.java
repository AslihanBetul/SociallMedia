package com.abm.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        System.out.println("SecurityFilterChain çalıştı");
//        httpSecurity
//                    .csrf(csrf->
//                            csrf.disable())
//                     .authorizeHttpRequests(authorize->
//                        authorize
//                                .requestMatchers("/login.html").permitAll() //istek eşleştiricisi geçerli buna izin ver
//                                .anyRequest().authenticated())//her istek authenticate olmalı
//
//                   //  .formLogin(Customizer.withDefaults());  //kullanıcı adı şifre
//                .formLogin(formLogin->
//                        formLogin
//                                .loginPage("/login.html").permitAll()); //permitAll gerek yok
//
//
//        return httpSecurity.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        System.out.println("SecurityFilterChain çalıştı");
        httpSecurity
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf->
                        csrf.disable())
                .authorizeHttpRequests(authorize->
                        authorize
                                .requestMatchers("/swagger-ui/**","/v3/api-docs/**","/api/v1/auth/findall/**","/api/v1/auth/verifyacountWithRabbit","/api/v1/auth/registerWithRabbit",
                                        "/api/v1/auth/login","/api/v1/auth/forgetMyPassword","/api/v1/auth/updatepaswword").permitAll()  //swagger özelinde /v3/api-docs
                                .requestMatchers("/api/v1/auth/delete/{*}").hasAuthority("ADMIN") //sadece admin özelliğine sahip olanlar giriş yapabilir
                                .requestMatchers("/api/v1/auth/findall").hasAuthority("USER") //sadece admin özelliğine sahip olanlar giriş yapabilir

                                .anyRequest().authenticated()); //her istek authenticate olmalı

//                  .formLogin(Customizer.withDefaults());  //kullanıcı adı şifre



        return httpSecurity.build();
    }
}
