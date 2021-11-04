package com.example.convertservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.entity"})

public class ConvertServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConvertServiceApplication.class, args);
    }

}
