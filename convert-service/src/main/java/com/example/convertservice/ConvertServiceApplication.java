package com.example.convertservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.entity"})
@EnableJpaRepositories
public class ConvertServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConvertServiceApplication.class, args);
    }

}
