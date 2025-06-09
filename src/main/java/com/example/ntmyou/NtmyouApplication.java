package com.example.ntmyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NtmyouApplication {
    public static void main(String[] args) {
        SpringApplication.run(NtmyouApplication.class, args);
    }
}
