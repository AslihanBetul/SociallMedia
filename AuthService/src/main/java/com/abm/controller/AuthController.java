package com.abm.controller;

import com.abm.constant.EndPoints;


import com.abm.dto.request.AccountActivationRequestDto;
import com.abm.dto.request.LoginRequestDto;
import com.abm.dto.request.RegisterRequestDto;
import com.abm.dto.request.RepasswordRequestDto;
import com.abm.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndPoints.AUTH)
@RequiredArgsConstructor
public class AuthController {
   private final AuthService authService;

   @PostMapping(EndPoints.REGISTER)
    public ResponseEntity<String >register(@RequestBody @Valid RegisterRequestDto dto){
       return ResponseEntity.ok(authService.register(dto));
   }

   @PostMapping("/registerWithRabbit")
    public ResponseEntity<Void >registerwithRabiitMq(@RequestBody @Valid RegisterRequestDto dto){
     authService.registerRabbitMQ(dto);
        return ResponseEntity.ok().build();
    }



   @PostMapping("/verifyacountWithRabbit")
    public ResponseEntity<String> verifyAccountRabbit(@RequestBody @Valid  AccountActivationRequestDto dto){
       return ResponseEntity.ok(authService.verifyAccountwithRabbit(dto));

   }

    @PostMapping(EndPoints.VERIFY_ACCOUNT)
    public ResponseEntity<String> verifyAccount(@RequestBody @Valid  AccountActivationRequestDto dto){
        return ResponseEntity.ok(authService.verifyAccount(dto));

    }

   @PostMapping(EndPoints.LOGIN)
    public ResponseEntity<String> login(@RequestBody @Valid  LoginRequestDto dto){
       return ResponseEntity.ok(authService.login(dto));

   }

   @DeleteMapping(EndPoints.DELETE+"/{id}")
    public ResponseEntity<String>delete(@PathVariable ("id") Long authId){
       return ResponseEntity.ok(authService.softDelete(authId));
   }


   @GetMapping("/getTokenById")
    public ResponseEntity<String> getTokenById(@RequestParam Long id){
       return ResponseEntity.ok(authService.getTokenById(id));
   }
   @GetMapping("/getIdFromToken")
    public ResponseEntity<Long> getIdFromToken(@RequestParam String token){
       return ResponseEntity.ok(authService.getIdFromToken(token));
   }
   @GetMapping("/getRoleFromToken")
    public ResponseEntity<String> getRoleFromToken(@RequestParam  String token){
       return ResponseEntity.ok(authService.getRoleFromToken(token));
   }
   @GetMapping("/getTokenByIdAndRole")
    public ResponseEntity<String> getTokenByIdAndRole(@RequestParam Long id,@RequestParam String role){
       return ResponseEntity.ok(authService.getTokenByIdAndRole(id,role));
   }

   @PutMapping("/updateEmail")
    public ResponseEntity<Void> updateEmail(@RequestParam String email, @RequestParam Long authId){
       authService.updateEmail(email,authId);
       return ResponseEntity.ok().build();
   }

   @GetMapping("/forgetMyPassword")
    public ResponseEntity<String> forgetMyPassword(@RequestParam String email){
       return ResponseEntity.ok(authService.forgetMyPassword(email));
   }
   @PutMapping("/updatepaswword")
    public ResponseEntity<String>updatePassword(@RequestBody RepasswordRequestDto repasswordRequestDto){
       authService.updatePassword(repasswordRequestDto);
       return ResponseEntity.ok("success");
   }







}


