package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class SecurityConfiguration {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        int strength = 4;
        return new BCryptPasswordEncoder(strength, new SecureRandom());
    }
}
