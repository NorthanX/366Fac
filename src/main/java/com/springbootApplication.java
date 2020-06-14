package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//
//@ComponentScan(basePackages = {"com"})
@SpringBootApplication
public class springbootApplication {

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(springbootApplication.class,args);


    }
}
