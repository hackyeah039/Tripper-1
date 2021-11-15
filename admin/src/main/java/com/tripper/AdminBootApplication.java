package com.tripper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.tripper")
public class AdminBootApplication {
    public static void main(String[] args){
        SpringApplication.run(AdminBootApplication.class, args);
    }
}
