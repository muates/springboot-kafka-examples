package com.muates.e.commerce.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class ECommerceEventApplication {
    public static void main(String[] args) {
        SpringApplication.run(ECommerceEventApplication.class, args);
    }
}
