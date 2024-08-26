package com.chototclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ChoTotCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChoTotCloneApplication.class, args);
    }

}
